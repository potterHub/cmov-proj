package models

type VoucherTemplateItem struct {
	IdVoucherTemplate int64 `json:"idVoucherTemplate,omit"`
	IdItem            int64 `json:"idItem"`
}
