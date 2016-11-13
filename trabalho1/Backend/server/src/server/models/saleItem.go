package models

type SaleItem struct {
	IdSale   int64 `json:"idSale,omitempty"`
	IdItem   int64 `json:"idItem"`
	Quantity int64 `json:"quantity"`
}
