package sqlite

import (
	"server/model"
	"fmt"
)

func (*Sqlite) GetItems() {

	rows, err := Sqlite.db.Query("SELECT * FROM item")
	if err != nil {
		panic(err)
		rows.Close()
	}
	for rows.Next() {
		var item model.Item
		err = rows.Scan(&item.IdItem, &item.IdItemType, &item.Price, &item.Description)
		if err != nil {
			panic(err)
		}
		fmt.Println(item.IdItem, item.IdItemType, item.Price, item.Description)
	}
	rows.Close()
}
