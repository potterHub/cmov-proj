package item

import (
	"github.com/pressly/chi"
	"server/helpers"
)

const MainPath = "/item"

func SubRoutes(router chi.Router) {
	router.Get("/", helpers.ReplyJson(getItems))
	router.Get("/:id", helpers.ReplyJson(getItem))
}
