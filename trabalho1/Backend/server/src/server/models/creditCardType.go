package models

type CreditCardType struct {
	IdCreditCardType int64  `json:"idCreditCardType"`
	Description      string `json:"description"`
}

func NewCreditCardType() *CreditCardType {
	return &CreditCardType{
		IdCreditCardType: -1,
	}
}

func ACreditCardType() CreditCardType {
	return CreditCardType{
		IdCreditCardType: -1,
	}
}
