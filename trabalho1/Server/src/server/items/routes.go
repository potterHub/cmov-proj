package items

import (
	"github.com/pressly/chi"
)

const MainPath = "/items"

func SubRoutes(router chi.Router) {
	router.Get("/items", getItems)
}
