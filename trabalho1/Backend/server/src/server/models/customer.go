package models

type Customer struct {
	IdCustomer int64      `json:"id"`
	CreditCard CreditCard `json:"creditCard"`
	Name       string     `json:"name"`
	Username   string     `json:"username"`
	Password   string     `json:"password,omitempty"`
	PIN        int        `json:"PIN"`
}

func NewCustomer() *Customer {
	return &Customer{
		IdCustomer: -1,
		CreditCard: ACreditCard(),
		PIN:        -1,
	}
}
