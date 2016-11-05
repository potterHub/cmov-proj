package models

type Sale struct {
	IdSale     int64      `json:"idSale"`
	IdCustomer int64      `json:"idCustomer,omitempty"`
	MyDateTime string     `json:"myDateTime"`
	Items      []*SaleItem `json:"items"`
	Vouchers   []*Voucher `json:"vouchers"`
}
