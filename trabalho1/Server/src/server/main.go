package main

import (
	"github.com/pressly/chi"
	"server/users"
	"server/terminals"
	"server/items"
)

func main() {
	router := chi.NewRouter()

	router.Route(users.MainPath, users.SubRoutes)
	router.Route(terminals.MainPath, terminals.SubRoutes)
	router.Route(items.MainPath, items.SubRoutes)
}
