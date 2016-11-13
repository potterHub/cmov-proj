package terminal

import (
	"github.com/pressly/chi"
	"server/helpers"
)

const MainPath = "/terminal"

func SubRoutes(router chi.Router) {
	router.Post("/order", helpers.Authenticated(helpers.PostJson(issueOrder)))
}
