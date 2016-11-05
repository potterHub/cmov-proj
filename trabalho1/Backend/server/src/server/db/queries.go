package db

import (
	"database/sql"
	"server/models"
	"strconv"
)

const ImagePath = "../../sqlite/image/"
const ImageItemPath = ImagePath + "item/"

func (db *DB) GetItems() ([]*models.Item, error) {
	rows, err := db.raw.Query(`
		SELECT item.*, itemtype.description FROM item
		JOIN itemtype on item.iditemtype = itemtype.iditemtype;
	`)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	items := make([]*models.Item, 0)
	for rows.Next() {
		item := models.NewItem()
		err = rows.Scan(
			&item.IdItem,
			&item.ItemType.IdItemType,
			&item.Price,
			&item.Name,
			&item.Description,
			&item.ItemType.Description)
		if err != nil {
			return nil, err
		}
		imageBase64Str, err := encodeFileToBase64(ImageItemPath + strconv.FormatInt(item.IdItem, 10))
		if err == nil {
			item.Image = imageBase64Str
		}
		items = append(items, item)
	}

	if err = rows.Err(); err != nil {
		return nil, err
	}

	return items, err
}

func (db *DB) GetItem(id string) (*models.Item, error) {
	row := db.raw.QueryRow(`
		SELECT item.*, itemtype.description
		FROM item
		JOIN itemtype on item.iditemtype = itemtype.iditemtype
		WHERE item.idItem = $1`, id)

	item := models.NewItem()
	err := row.Scan(
		&item.IdItem,
		&item.ItemType.IdItemType,
		&item.Price,
		&item.Name,
		&item.Description,
		&item.ItemType.Description)

	if err != nil {
		return nil, err
	}

	imageBase64Str, err := encodeFileToBase64(ImageItemPath + strconv.FormatInt(item.IdItem, 10))
	if err == nil {
		item.Image = imageBase64Str
	}

	return item, err
}

func (db *DB) GetCustomer(username string) (*models.Customer, error) {
	row := db.raw.QueryRow(`
		SELECT customer.*, creditCard.idCreditCardType, creditCard.code, creditCard.year, creditCard.month
		FROM customer
		JOIN creditCard on customer.idCreditCard = creditCard.idCreditCard
		WHERE username = $1`, username)

	customer := models.NewCustomer()
	err := row.Scan(
		&customer.IdCustomer,
		&customer.CreditCard.IdCreditCard,
		&customer.Name,
		&customer.Username,
		&customer.Password,
		&customer.PIN,
		&customer.CreditCard.IdCreditCardType,
		&customer.CreditCard.Code,
		&customer.CreditCard.Year,
		&customer.CreditCard.Month)

	if err != nil {
		return nil, err
	}

	return customer, err
}

func (db *DB) GetCreditCardType(idCreditCardType int64) (*models.CreditCardType, error) {
	row := db.raw.QueryRow(`
		SELECT * FROM creditCardType
		WHERE idCreditCardType = $1`, idCreditCardType)

	creditCardType := models.NewCreditCardType()
	err := row.Scan(
		&creditCardType.IdCreditCardType,
		&creditCardType.Description)

	if err != nil {
		return nil, err
	}

	return creditCardType, err
}

func (db *DB) GetCreditCardTypes() ([]*models.CreditCardType, error) {
	rows, err := db.raw.Query(`
		SELECT * FROM creditCardType
	`)
	if err != nil {
		return nil, err
	}
	defer rows.Close()
	creditCardTypes := make([]*models.CreditCardType, 0)
	for rows.Next() {
		creditCardType := models.NewCreditCardType()
		err = rows.Scan(
			&creditCardType.IdCreditCardType,
			&creditCardType.Description)
		if err != nil {
			return nil, err
		}
		creditCardTypes = append(creditCardTypes, creditCardType)
	}

	if err = rows.Err(); err != nil {
		return nil, err
	}

	return creditCardTypes, err
}

func (db *DB) GetVouchers(customerId int64, saleId int64) ([]*models.Voucher, error) {

	var voucherRows *sql.Rows
	var err error
	if (saleId > 0) {
		voucherRows, err = db.raw.Query(`
		SELECT
		voucher.idVoucher, voucher.code, voucher.gotVoucher,
		voucherTemplate.idVoucherTemplate, voucherTemplate.description,
		voucherTemplate.value, voucherTemplateType.idVoucherTemplateType,
		voucherTemplateType.description, itemType.idItemType, itemType.description
		FROM voucher
		JOIN voucherTemplate on voucherTemplate.idVoucherTemplate = voucher.idVoucherTemplate
		JOIN voucherTemplateType on voucherTemplateType.idVoucherTemplateType = voucherTemplate.idVoucherTemplateType
		LEFT JOIN itemType on itemType.idItemType = voucherTemplate.idItemType
		WHERE voucher.idCustomer = $1 AND voucher.idSale = $2
	`, customerId, saleId)
	} else {
		voucherRows, err = db.raw.Query(`
		SELECT
		voucher.idVoucher, voucher.code, voucher.gotVoucher,
		voucherTemplate.idVoucherTemplate, voucherTemplate.description,
		voucherTemplate.value, voucherTemplateType.idVoucherTemplateType,
		voucherTemplateType.description, itemType.idItemType, itemType.description
		FROM voucher
		JOIN voucherTemplate on voucherTemplate.idVoucherTemplate = voucher.idVoucherTemplate
		JOIN voucherTemplateType on voucherTemplateType.idVoucherTemplateType = voucherTemplate.idVoucherTemplateType
		LEFT JOIN itemType on itemType.idItemType = voucherTemplate.idItemType
		WHERE voucher.idSale IS NULL AND voucher.usedVoucher IS NULL AND voucher.idCustomer = $1
	`, customerId)
	}

	if err != nil {
		return nil, err
	}
	defer voucherRows.Close()

	type itemTypeNullable struct {
		IdItemType  sql.NullInt64
		Description sql.NullString
	}

	vouchers := make([]*models.Voucher, 0)
	for voucherRows.Next() {
		tempItemType := itemTypeNullable{}
		voucher := new(models.Voucher)
		err := voucherRows.Scan(
			&voucher.IdVoucher,
			&voucher.Code,
			&voucher.GotVoucher,
			&voucher.VoucherTemplate.IdVoucherTemplate,
			&voucher.VoucherTemplate.Description,
			&voucher.VoucherTemplate.Value,
			&voucher.VoucherTemplate.VoucherTemplateType.IdVoucherTemplateType,
			&voucher.VoucherTemplate.VoucherTemplateType.Description,
			&tempItemType.IdItemType,
			&tempItemType.Description)

		if err != nil {
			return nil, err
		}

		if tempItemType.IdItemType.Valid && tempItemType.Description.Valid {
			idItemType, err := tempItemType.IdItemType.Value()
			if err != nil {
				return nil, err
			}
			description, err := tempItemType.Description.Value()
			if err != nil {
				return nil, err
			}
			voucher.VoucherTemplate.ItemType = new(models.ItemType)
			voucher.VoucherTemplate.ItemType.IdItemType = idItemType.(int64)
			voucher.VoucherTemplate.ItemType.Description = description.(string)
		}

		voucherTemplateItemRows, err := db.raw.Query(`
		SELECT idItem
		FROM voucherTemplateItem
		WHERE idVoucherTemplate = $1`, voucher.VoucherTemplate.IdVoucherTemplate)
		itemsIds := make([]int64, 0)
		for voucherTemplateItemRows.Next() {
			var itemId int64
			err = voucherTemplateItemRows.Scan(&itemId)
			if err != nil {
				voucherTemplateItemRows.Close()
				return nil, err
			}
			itemsIds = append(itemsIds, itemId)
		}

		err = voucherTemplateItemRows.Err()
		if err == nil {
			voucher.VoucherTemplate.ItemsIds = itemsIds
		} else if err != sql.ErrNoRows {
			voucherTemplateItemRows.Close()
			return nil, err
		}
		voucherTemplateItemRows.Close()

		vouchers = append(vouchers, voucher)
	}

	if err = voucherRows.Err(); err != nil {
		return nil, err
	}

	return vouchers, err
}

func (db *DB) GetOrders(idCustomer int64) ([]*models.Sale, error) {
	saleRows, err := db.raw.Query(`
		SELECT idSale, myDateTime
		FROM sale
		WHERE idCustomer = $1`, idCustomer)

	if err != nil {
		return nil, err
	}
	defer saleRows.Close()

	sales := make([]*models.Sale, 0)
	for saleRows.Next() {
		sale := new(models.Sale)
		err := saleRows.Scan(
			&sale.IdSale,
			&sale.MyDateTime)

		if err != nil {
			return nil, err
		}

		// Get items
		saleItemsRows, err := db.raw.Query(`
		SELECT idItem, quantity
		FROM saleItem
		WHERE idSale = $1`, sale.IdSale)
		saleItems := make([]*models.SaleItem, 0)
		for saleItemsRows.Next() {
			saleItem := new(models.SaleItem)
			err = saleItemsRows.Scan(&saleItem.IdItem, &saleItem.Quantity)
			if err != nil {
				saleItemsRows.Close()
				return nil, err
			}
			saleItems = append(saleItems, saleItem)
		}

		err = saleItemsRows.Err()
		if err == nil {
			sale.Items = saleItems
		} else if err != sql.ErrNoRows {
			saleItemsRows.Close()
			return nil, err
		}
		saleItemsRows.Close()

		// Get vouchers
		vouchers, err := db.GetVouchers(idCustomer, sale.IdSale)
		if err != nil {
			return nil, err
		}
		sale.Vouchers = vouchers

		sales = append(sales, sale)
	}

	if err = saleRows.Err(); err != nil {
		return nil, err
	}

	return sales, err
}
