package terminals

import (
	"github.com/pressly/chi"
)

const MainPath = "/terminals"

func SubRoutes(router chi.Router) {
	router.Post("/order", issueOrder)
	router.Get("/blacklist", getBlacklist)
}