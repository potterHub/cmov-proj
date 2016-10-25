package main

import (
	"github.com/pressly/chi"
	"github.com/alexedwards/scs/engine/memstore"
	"github.com/alexedwards/scs/session"
	"server/users"
	"server/terminals"
	"server/items"
	"net/http"
)

func main() {
	router := chi.NewRouter()

	engine := memstore.New(0)
	sessionManager := session.Manage(engine)

	router.Route(users.MainPath, users.SubRoutes)
	router.Route(terminals.MainPath, terminals.SubRoutes)
	router.Route(items.MainPath, items.SubRoutes)
	http.ListenAndServe(":80", sessionManager(router))
}