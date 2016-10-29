package db

import "server/models"

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
		item := new(models.Item)
		err = rows.Scan(
			&item.IdItem,
			&item.ItemType.IdItemType,
			&item.Price,
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
		SELECT item.*, itemtype.description FROM item
		JOIN itemtype on item.iditemtype = itemtype.iditemtype
		WHERE item.idItem = $1`, id)

	item := new(models.Item)
	err := row.Scan(
		&item.IdItem,
		&item.ItemType.IdItemType,
		&item.Price,
		&item.Description,
		&item.ItemType.Description)

	if err != nil {
		return nil, err
	}

	return item, err
}