package models

type ItemType struct {
	IdItemType  	int64	`json:"idItemType"`
	Description 	string	`json:"description"`
}

func NewItemType() *ItemType {
	return &ItemType{
		IdItemType: -1,
	}
}

func AItemType() ItemType {
	return ItemType{
		IdItemType: -1,
	}
}
