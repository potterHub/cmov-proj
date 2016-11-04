package customer

import (
	"github.com/pressly/chi"
	"server/helpers"
)

const MainPath = "/customer"

func SubRoutes(router chi.Router) {
	router.Post("/register", helpers.PostJson(helpers.ReplyJson(registerCustomer)))
	router.Post("/login", helpers.PostJson(helpers.ReplyJson(loginCustomer)))
	router.Get("/voucher", getCustomerVouchers)
	router.Get("/order", getCustomerOrders)
}
