package db

import (
	"server/models"
)

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