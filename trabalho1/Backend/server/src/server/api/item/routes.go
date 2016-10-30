package item

import (
	"github.com/pressly/chi"
)

const MainPath = "/item"

func SubRoutes(router chi.Router) {
	router.Get("/", getItems)
	router.Get("/:id", getItem)
}
