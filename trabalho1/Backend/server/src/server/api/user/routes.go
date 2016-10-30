package user

import (
	"github.com/pressly/chi"
)

const MainPath = "/user"

func SubRoutes(router chi.Router) {
	router.Post("/register", registerUser)
	router.Post("/login", authenticateUser)
	router.Get("/logout", logoutUser)
	router.Get("/vouchers", getUserVouchers)
	router.Get("/orders", getUserOrders)
	router.Route("/profile", subRoutesUserManagement)
}

func subRoutesUserManagement(router chi.Router) {
	router.Get("/", getUser)
	router.Put("/", updateUser)
	router.Delete("/", deleteUser)
}
