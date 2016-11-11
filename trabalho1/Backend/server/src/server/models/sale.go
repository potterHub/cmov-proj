package models

type Sale struct {
	IdSale     int64       `json:"idSale"`
	IdCustomer int64       `json:"idCustomer,omitempty"`
	MyDateTime string      `json:"myDateTime"`
	Total      float64     `json:"total,omitempty"`
	Items      []*SaleItem `json:"items"`
	Vouchers   []*Voucher  `json:"vouchers"`
}

func NewSale() *Sale {
	return &Sale{
		IdSale:     -1,
		IdCustomer: -1,
		Items:      nil,
		Vouchers:   nil,
	}
}
