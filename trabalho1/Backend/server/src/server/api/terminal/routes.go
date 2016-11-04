package terminal

import (
	"github.com/pressly/chi"
)

const MainPath = "/terminal"

func SubRoutes(router chi.Router) {
	router.Post("/order", issueOrder)
	router.Get("/blacklist", getBlacklist)
}
