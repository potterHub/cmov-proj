package models

type CreditCard struct {
	IdCreditCard     int64	`json:"idCreditCard"`
	IdCreditCardType int64	`json:"idCreditCardType"`
	Code		 string `json:"code"`
	Year		 int    `json:"year"`
	Month 		 int	`json:"month"`
}

func NewCreditCard() *CreditCard {
	return &CreditCard{
		IdCreditCard: -1,
		IdCreditCardType: -1,
		Year: -1,
		Month: -1,
	}
}

func ACreditCard() CreditCard {
	return CreditCard{
		IdCreditCard: -1,
		IdCreditCardType: -1,
		Year: -1,
		Month: -1,
	}
}