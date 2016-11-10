package terminal

import (
	"database/sql"
	"encoding/json"
	"net/http"
	"server/globals/sqlite"
	"server/helpers"
	"server/models"
	"strconv"
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

	// Check if all fields are set and correct
	error := ""
	if newSale.IdCustomer != claims.IdCustomer {
		error += "Cannot make an order by another customer "
	}
	if newSale.Items == nil || len(newSale.Items) == 0 {
		error += "A sale must have at least one item "
	}

	if newSale.Vouchers != nil {
		vouchersLength := len(newSale.Vouchers)
		if vouchersLength > 3 {
			error += "At most 3 vouchers "
		} else if vouchersLength > 0 {
			vouchersWithMeta, err := sqlite.DB.GetVouchersSale(claims.IdCustomer, newSale.Vouchers)
			if err != nil {
				http.Error(w, "Invalid vouchers", 400)
				return
			}
			if len(vouchersWithMeta) != vouchersLength {
				error += "Invalid vouchers "
			}

			templates, err := sqlite.DB.GetVoucherTemplateTypes()
			if err != nil {
				http.Error(w, "Failed fetching voucherTemplatesTypes from db", 500)
				return
			}

			oneGlobalDiscount := false
			for _, voucher := range vouchersWithMeta {
				if voucher.IdSale != 0 {
					error = strconv.FormatInt(voucher.IdVoucher, 10) + ":" + voucher.Code + " voucher already used "
				}
				if voucher.VoucherTemplate.VoucherTemplateType.IdVoucherTemplateType == (*templates)["Global Discount"] {
					if oneGlobalDiscount {
						error += "Can only have one global discount "
						break
					}
					oneGlobalDiscount = true
				}
			}
		}
	}

	if newSale.Items == nil || len(newSale.Items) == 0 {
		error += "Missing basket items "
	}

	if error != "" {
		http.Error(w, error, 400)
		return
	}

	// Calculate the value of items to generate custom vouchers
	newSale.Total, err = sqlite.DB.GetItemsTotal(newSale.Items)
	if err != nil {
		if err == sql.ErrNoRows {
			http.Error(w, "Invalid item in basket", 400)
		} else {
			http.Error(w, "Failed calculating sale total price", 500)
		}
		return
	}

	// Insert the new sale, update used vouchers, and generate new vouchers
	err = sqlite.DB.InsertSale(claims, newSale)
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}

	// On success return the price of all the items and the idSale
	w.Write([]byte(`{"idSale":` + strconv.FormatInt(newSale.IdSale, 10) + `,"total":` + strconv.FormatFloat(newSale.Total, 'f', 2, 32) + `}`))
}

func getBlacklist(w http.ResponseWriter, r *http.Request) {

}
