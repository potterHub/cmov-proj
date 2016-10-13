package main

import (
	"github.com/pressly/chi"
	"server/users"
	"server/terminals"
)

func main() {
	router := chi.NewRouter()

	router.Route(users.MainPath, users.SubRoutes)
	router.Route(terminals.MainPath, terminals.SubRoutes)
}
