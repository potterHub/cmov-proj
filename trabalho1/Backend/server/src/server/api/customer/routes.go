package customer

import (
	"github.com/pressly/chi"
	"server/helpers"
)

const MainPath = "/customer"

func SubRoutes(router chi.Router) {
	router.Post("/register", helpers.ReplyJson(registerCustomer))
	router.Post("/login", loginCustomer)
	router.Get("/voucher", getCustomerVouchers)
	router.Get("/order", getCustomerOrders)
}
