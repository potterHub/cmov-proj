package models

type Customer struct {
	IdCustomer      int64		`json:"id"`
	CreditCard	CreditCard	`json:"creditCard"`
	Name		string		`json:"name"`
	Username 	string		`json:"username"`
	Password	string		`json:"password"`
	PIN 		int		`json:"PIN"`
}