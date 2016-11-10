package db

import (
	"database/sql"
	"errors"
	"server/authentication"
	"server/models"
	"time"
	"math/rand"
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
	return nil
}

func (db *DB) InsertSale(claims *authentication.MyClaims, sale *models.Sale) (err error) {
	tx, err := db.raw.Begin()
	defer func() {
		if err != nil {
			tx.Rollback()
			return
		}
		err = tx.Commit()
	}()

	previousSalesTotal, err := db.GetSalesTotal(sale.IdCustomer)
	if err != nil {
		return err
	}

	res, err := tx.Exec(`
	INSERT INTO sale(idSale, idCustomer, myDateTime, total)
	VALUES (NULL,?,?,?)`,
		sale.IdCustomer,
		time.Now().UTC().String(),
		sale.Total)
	if err != nil {
		return err
	}

	sale.IdSale, err = res.LastInsertId()
	if err != nil {
		return err
	}

	elementsPer := 3
	var values string
	itemsLength := len(sale.Items)
	argsFactory := make([]interface{}, itemsLength*elementsPer)
	for i, _ := range sale.Items {
		values = "(?,?,?)" + values
		if i != (itemsLength - 1) {
			values = "," + values
		}
		offset := i * elementsPer
		argsFactory[offset] = sale.IdSale
		argsFactory[offset+1] = sale.Items[i].IdItem
		argsFactory[offset+2] = sale.Items[i].Quantity
	}

	res, err = tx.Exec(`
	INSERT INTO saleItem(idSale, idItem, quantity)
	VALUES `+values, argsFactory...)
	if err != nil {
		return err
	}

	// Disable used vouchers
	var ins string
	vouchersLength := len(sale.Vouchers)
	argsFactory = make([]interface{}, 2+vouchersLength)

	now := time.Now().UTC()
	start := 0
	argsFactory[start] = sale.IdSale
	start++
	for i, voucher := range sale.Vouchers {
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
		return err
	}

	// Generate vouchers
	stmt, err := tx.Prepare(`
	INSERT INTO voucher(idVoucher,idVoucherTemplate,idCustomer,idSale,code,gotVoucher)
	VALUES
		(NULL,
		(SELECT idVoucherTemplate FROM voucherTemplate WHERE description LIKE ?),
		?, NULL, ?, ?)`)
	if err != nil {
		return err
	}
	defer stmt.Close()

	if sale.Total >= 20.00 {
		discount := "%free popcorn%"
		if rand.Intn(2) == 0 {
			discount = "%free coffee%"
		}
		err = insertNewVoucher(stmt, discount, sale.IdCustomer, now)
		if err != nil {
			return err
		}
	}

	var nGlobalDiscounts int64 = int64((previousSalesTotal+sale.Total)/100) - int64(previousSalesTotal/100)
	for ; nGlobalDiscounts > 0; nGlobalDiscounts-- {
		err = insertNewVoucher(stmt, "%5%% Discount%", sale.IdCustomer, now)
		if err != nil {
			return err
		}
	}

	return nil
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
