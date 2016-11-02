package main

import (
	"github.com/pressly/chi"
	"math/rand"
	"net/http"
	"os"
	"path/filepath"
	"server/api/cardTypes"
	"server/api/customer"
	"server/api/item"
	"server/api/terminal"
	"server/authentication"
	"server/db"
	"server/globals"
	"time"
)

func main() {
	rand.Seed(time.Now().UTC().UnixNano())
	cdToBinary()

	db := db.Connect(globals.Sqlite3DbPath)
	defer db.Close()
	globals.DB = db

	router := chi.NewRouter()
	router.Use(authentication.GetToken())

	router.Route(cardTypes.MainPath, cardTypes.SubRoutes)
	router.Route(customer.MainPath, customer.SubRoutes)
	router.Route(terminal.MainPath, terminal.SubRoutes)
	router.Route(item.MainPath, item.SubRoutes)

	http.ListenAndServe(":8080", router)
}

func cdToBinary() {
	bin_dir, err := filepath.Abs(filepath.Dir(os.Args[0]))
	if err != nil {
		panic(err)
	}

	err = os.Chdir(bin_dir)
	if err != nil {
		panic(err)
	}
}
