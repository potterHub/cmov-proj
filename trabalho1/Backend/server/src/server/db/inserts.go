package db

import (
	"database/sql"
	"errors"
	"math/rand"
	"server/authentication"
	"server/models"
	"time"
)

func (db *DB) InsertCustomer(customer *models.Customer) (err error) {
	tx, err := db.raw.Begin()
	defer func() {
		if err != nil {
			tx.Rollback()
			return
		}
		err = tx.Commit()
	}()

	res, err := tx.Exec(`
	INSERT INTO creditCard(idCreditCard, idCreditCardType, code, year, month)
	VALUES (NULL,?,?,?,?)`,
		customer.CreditCard.IdCreditCardType,
		customer.CreditCard.Code,
		customer.CreditCard.Year,
		customer.CreditCard.Month)
	if err != nil {
		return err
	}

	idCreditCard, err := res.LastInsertId()
	if err != nil {
		return err
	}
	customer.CreditCard.IdCreditCard = idCreditCard

	res, err = tx.Exec(`
	INSERT INTO customer(idCustomer, idCreditCard, name, username, password, PIN)
	VALUES (NULL,?,?,?,?,?)`,
		idCreditCard,
		customer.Name,
		customer.Username,
		customer.Password,
		customer.PIN)
	if err != nil {
		return err
	}

	idCustomer, err := res.LastInsertId()
	if err != nil {
		return err
	}

	customer.IdCustomer = idCustomer
	return err
}

func (db *DB) InsertSale(claims *authentication.MyClaims,
	items []*models.Item, itemsQuantities map[int64]int64, total float64,
	vouchers []*models.Voucher) (int64, error) {
	tx, err := db.raw.Begin()
	defer func() {
		if err != nil {
			tx.Rollback()
			return
		}
		err = tx.Commit()
	}()

	now := time.Now().UTC()

	previousSalesTotal, err := db.GetSalesTotal(claims.IdCustomer)
	if err != nil {
		return 0, err
	}

	res, err := tx.Exec(`
	INSERT INTO sale(idSale, idCustomer, myDateTime, total)
	VALUES (NULL,?,?,?)`,
		claims.IdCustomer,
		now,
		total)
	if err != nil {
		return 0, err
	}

	idSale, err := res.LastInsertId()
	if err != nil {
		return 0, err
	}

	elementsPer := 3
	var values string
	itemsLength := len(items)
	argsFactory := make([]interface{}, itemsLength*elementsPer)
	for i, item := range items {
		values = "(?,?,?)" + values
		if i != (itemsLength - 1) {
			values = "," + values
		}
		offset := i * elementsPer
		argsFactory[offset] = idSale
		argsFactory[offset+1] = item.IdItem
		argsFactory[offset+2] = itemsQuantities[item.IdItem]
	}

	res, err = tx.Exec(`
	INSERT INTO saleItem(idSale, idItem, quantity)
	VALUES `+values, argsFactory...)
	if err != nil {
		return 0, err
	}

	// Disable used vouchers
	var ins string
	vouchersLength := len(vouchers)
	argsFactory = make([]interface{}, 2+vouchersLength)

	start := 0
	argsFactory[start] = idSale
	start++
	for i, voucher := range vouchers {
		offset := start + i
		argsFactory[offset] = voucher.IdVoucher
		ins = "?" + ins
		if i != (vouchersLength - 1) {
			ins = "," + ins
		}
	}
	res, err = tx.Exec(`
	UPDATE voucher
	SET idSale = ?
	WHERE idVoucher IN (`+ins+`)`, argsFactory...)
	if err != nil {
		return 0, err
	}

	// Generate vouchers
	stmt, err := tx.Prepare(`
	INSERT INTO voucher(idVoucher,idVoucherTemplate,idCustomer,idSale,code,gotVoucher)
	VALUES
		(NULL,
		(SELECT idVoucherTemplate FROM voucherTemplate WHERE description LIKE ?),
		?, NULL, ?, ?)`)
	if err != nil {
		return 0, err
	}
	defer stmt.Close()

	if total >= 20.00 {
		discount := "%free popcorn%"
		if rand.Intn(2) == 0 {
			discount = "%free coffee%"
		}
		err = insertNewVoucher(stmt, discount, claims.IdCustomer, now)
		if err != nil {
			return 0, err
		}
	}

	var nGlobalDiscounts int64 = int64((previousSalesTotal+total)/100) - int64(previousSalesTotal/100)
	for ; nGlobalDiscounts > 0; nGlobalDiscounts-- {
		err = insertNewVoucher(stmt, "%5%% Discount%", claims.IdCustomer, now)
		if err != nil {
			return 0, err
		}
	}

	return idSale, err
}

func insertNewVoucher(stmt *sql.Stmt, template string, customer int64, now time.Time) error {
	for i := 1000; i > 0; i-- {
		code, err := generateVoucherCode()
		if err != nil {
			return err
		}
		_, err = stmt.Exec(template, customer, code, now)
		if err == nil {
			return nil
		}
		time.Sleep(time.Microsecond)
	}
	return errors.New("Failed creating new voucher")
}
