package cardTypes

import (
	"github.com/pressly/chi"
	"server/helpers"
)

const MainPath = "/cardtypes"

func SubRoutes(router chi.Router) {
	router.Get("/", helpers.ReplyJson(getCreditCardTypes))
}
