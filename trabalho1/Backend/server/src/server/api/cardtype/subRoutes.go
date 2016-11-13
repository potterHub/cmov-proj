package cardtype

import (
	"github.com/pressly/chi"
	"server/helpers"
)

const MainPath = "/cardtype"

func SubRoutes(router chi.Router) {
	router.Get("/", helpers.ReplyJson(getCreditCardTypes))
}
