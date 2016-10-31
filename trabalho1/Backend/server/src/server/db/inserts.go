package db

import (
	"server/models"
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
