package models

type Voucher struct {
	IdVoucher       int64           `json:"idVoucher"`
	VoucherTemplate VoucherTemplate `json:"voucherTemplate"`
	IdCustomer      int64           `json:"idCustomer,omitempty"`
	IdSale          int64           `json:"idSale,omitempty"`
	Code            string          `json:"code"`
	GotVoucher      string          `json:"gotVoucher,omitempty"`
}
