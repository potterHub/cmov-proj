package users

import (
	"github.com/pressly/chi"
)

const MainPath = "/users"

func SubRoutes(router chi.Router) {
	router.Post("/register", createUser)
	router.Post("/login", authenticateUser)
	router.Get("/logout", logoutUser)
	router.Get("/vouchers", getUserVouchers)
	router.Route("/profile", subRoutesUserManagement)
	router.Get("/orders", getUserOrders)
}

func subRoutesUserManagement(router chi.Router) {
	router.Get("/", getUser)
	router.Put("/", updateUser)
	router.Delete("/", deleteUser)
}