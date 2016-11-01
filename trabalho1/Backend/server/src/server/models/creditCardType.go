package models

type CreditCardType struct {
	idCreditCardType int64	`json:"idCreditCardType"`
	description 	 string `json:"description"`
}

func NewCreditCardType() *CreditCardType {
	return &CreditCardType{
		idCreditCardType: -1,
	}
}

func ACreditCardType() CreditCardType {
	return CreditCardType{
		idCreditCardType: -1,
	}
}
