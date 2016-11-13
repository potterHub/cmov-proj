package models

type Item struct {
	IdItem      int64    `json:"idItem"`
	ItemType    ItemType `json:"itemType"`
	Price       float64  `json:"price"`
	Name        string   `json:"name"`
	Description string   `json:"description"`
	Image       string   `json:"image"`
}

func NewItem() *Item {
	return &Item{
		IdItem:   -1,
		ItemType: AItemType(),
		Price:    -1,
	}
}

type Items []*Item

func (items Items) Len() int {
	return len(items)
}

func (items Items) Less(i, j int) bool {
	return items[i].Price > items[j].Price
}

func (items Items) Swap(i, j int) {
	items[i], items[j] = items[j], items[i]
}
