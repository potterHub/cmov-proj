package models

type Item struct {
	IdItem      	int64		`json:"idItem"`
	ItemType 	ItemType	`json:"itemType"`
	Price       	float64		`json:"price"`
	Name		string		`json:"name"`
	Description 	string		`json:"description"`
}