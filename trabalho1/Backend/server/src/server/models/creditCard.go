package models

type CreditCard struct {
	IdCreditCard     int64	`json:"idCreditCard"`
	IdCreditCardType int64	`json:"idCreditCardType"`
	Code		 string `json:"code"`
	Year		 int    `json:"year"`
	Month 		 int	`json:"month"`
}
