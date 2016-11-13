package terminal

import (
	"encoding/json"
	"net/http"
	"server/globals/sqlite"
	"server/helpers"
	"server/models"
	"sort"
	"strconv"
	"time"
)

func issueOrder(w http.ResponseWriter, r *http.Request) {
	claims := helpers.GetAuth(r)
	if claims == nil {
		http.Error(w, "Cannot make an order without the customer token", 400)
		return
	}

	// Decoding Input
	decoder := json.NewDecoder(r.Body)
	newSale := models.NewSale()
	err := decoder.Decode(newSale)
	if err != nil {
		http.Error(w, "Invalid JSON", 400)
		return
	}

	// Check fields
	if newSale.IdCustomer != claims.IdCustomer {
		http.Error(w, "Cannot make an order by another customer", 400)
		return
	}

	now := time.Now()
	cardYear, cardMonth, err := sqlite.DB.GetCreditCardYearMonth(newSale.IdCustomer)
	if cardYear < now.Year() || cardMonth < int(now.Month()) {
		// TODO update blacklist and send json, expired card.
		http.Error(w, "Credit card expired", 400)
		return
	}

	if newSale.Items == nil || len(newSale.Items) == 0 {
		http.Error(w, "A sale must have at least one item", 400)
		return
	}

	// Get items with metadata
	items := make([]*models.Item, 0)
	itemsQuantities := make(map[int64]int64)
	for _, postItem := range newSale.Items {
		if postItem.Quantity <= 0 {
			http.Error(w, "Quantity must be greater than 0", 400)
			return
		}
		if itemsQuantities[postItem.IdItem] != 0 {
			itemsQuantities[postItem.IdItem] += postItem.Quantity
			continue
		}
		item, err := sqlite.DB.GetItem(strconv.FormatInt(postItem.IdItem, 10))
		if err != nil {
			http.Error(w, "Invalid item", 400)
			return
		}
		itemsQuantities[postItem.IdItem] += postItem.Quantity
		items = append(items, item)
	}

	var globalDiscountVoucher *models.Voucher
	popcornVouchers := make([]*models.Voucher, 0)
	coffeeVouchers := make([]*models.Voucher, 0)

	var popcornItemsCount int64
	var coffeeItemsCount int64
	var popcornPrice float64
	coffeeEligible := models.Items{}
	for _, item := range items {
		if item.Name == "Popcorn" {
			popcornItemsCount = itemsQuantities[item.IdItem]
			popcornPrice = item.Price
		} else if item.ItemType.Description == "Coffee" {
			coffeeItemsCount += itemsQuantities[item.IdItem]
			coffeeEligible = append(coffeeEligible, item)
		}
	}
	sort.Sort(coffeeEligible)

	if voucherDuplicateCheck := make(map[int64]bool); newSale.Vouchers != nil {
		vouchersLength := len(newSale.Vouchers)
		if vouchersLength > 3 {
			http.Error(w, "At most 3 vouchers", 400)
			return
		} else if vouchersLength > 0 {
			for _, voucher := range newSale.Vouchers {
				if voucherDuplicateCheck[voucher.IdVoucher] {
					// Ignore duplicate vouchers
					continue
				}
				voucherDuplicateCheck[voucher.IdVoucher] = true

				err = sqlite.DB.GetVoucherSale(newSale.IdCustomer, voucher)
				if err != nil {
					// TODO update blacklist and send json, wrong voucher
					http.Error(w, "Invalid voucher", 400)
					return
				}

				/*** Ignore the vouchers that do not make sense and grab the values for the discount ***/
				// Coded just for the cases asked by the professor. Not generic.
				// Only 5% discount, Free popcorn, and Free coffee
				// The correct generic implementation would have a
				// switch for voucher.VoucherTemplate.VoucherTemplateType.Description

				// Global Discount Vouchers, only one, the bigger if multiple
				switch voucher.VoucherTemplate.Description {
				case "5% Discount":
					if globalDiscountVoucher == nil {
						globalDiscountVoucher = voucher
					}
				case "Free popcorn":
					if int64(len(popcornVouchers)) >= popcornItemsCount {
						continue
					}
					popcornVouchers = append(popcornVouchers, voucher)
				case "Free coffee":
					if int64(len(coffeeVouchers)) >= coffeeItemsCount {
						continue
					}
					coffeeVouchers = append(coffeeVouchers, voucher)
				default:
					http.Error(w, "Missing voucherTemplateType implementation", 500)
					return
				}
			}
		}
	}

	// Calculate totalWithoutDiscount, total, discount
	var totalWithoutDiscount float64
	vouchers := make([]*models.Voucher, 0)
	for _, item := range items {
		totalWithoutDiscount += item.Price * float64(itemsQuantities[item.IdItem])
	}
	var discount float64
	// Selected Global discount
	if globalDiscountVoucher != nil {
		discount = totalWithoutDiscount * globalDiscountVoucher.VoucherTemplate.Value
		vouchers = append(vouchers, globalDiscountVoucher)
	}
	// Selected Free popcorn vouchers
	for _, voucher := range popcornVouchers {
		discount += popcornPrice
		vouchers = append(vouchers, voucher)
	}
	// Selected Free coffee vouchers
	itemsQuantitiesCopy := make(map[int64]int64)
	for k, v := range itemsQuantities {
		itemsQuantitiesCopy[k] = v
	}
	coffeeEligibleOffset := 0
	for _, voucher := range coffeeVouchers {
		coffee := coffeeEligible[coffeeEligibleOffset]
		itemsQuantitiesCopy[coffee.IdItem] -= 1
		if itemsQuantitiesCopy[coffee.IdItem] == 0 {
			coffeeEligibleOffset += 1
		}
		discount += coffee.Price
		vouchers = append(vouchers, voucher)
	}

	total := helpers.RoundFloat(totalWithoutDiscount-discount, 2)
	totalWithoutDiscount = helpers.RoundFloat(totalWithoutDiscount, 2)
	discount = helpers.RoundFloat(discount, 2)

	// Insert the new sale, update used vouchers, and generate new vouchers
	idSale, err := sqlite.DB.InsertSale(claims, items, itemsQuantities, total, vouchers)
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}

	// On success return the price of all the items and the idSale
	w.Write([]byte(`{"idSale":` + strconv.FormatInt(idSale, 10) +
		`,"total":` + strconv.FormatFloat(total, 'f', 2, 32) +
		`,"discount":` + strconv.FormatFloat(discount, 'f', 2, 32) + `}`))
}
