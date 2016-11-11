package models

type VoucherTemplate struct {
	IdVoucherTemplate   int64               `json:"idVoucherTemplate"`
	VoucherTemplateType VoucherTemplateType `json:"voucherTemplateType"`
	ItemType            *ItemType           `json:"itemType,omitempty"`
	ItemsIds            []int64             `json:"items,omitempty"`
	Description         string              `json:"description"`
	Value               float64             `json:"value"`
}
