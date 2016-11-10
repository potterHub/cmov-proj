package main

import (
	"github.com/pressly/chi"
	"math/rand"
	"net/http"
	"os"
	"path/filepath"
	"server/api/cardtype"
	"server/api/customer"
	"server/api/item"
	"server/api/terminal"
	"server/authentication"
	"server/db"
	"server/globals/keys"
	"server/globals/sqlite"
	"server/keypair"
	"time"
)

func main() {
	rand.Seed(time.Now().UTC().UnixNano())
	cdToBinary()

	db := db.Connect(sqlite.Path)
	defer db.Close()
	sqlite.DB = db

	var exists bool = false
	keys.Server, exists = keypair.CreateKeypairIfNoExists(keys.ServerKeypairPath)
	keys.Server.SaveKeypair(keys.ServerKeypairPath)
	if !exists {
		keys.Server.ExportPublicKey(keys.ServerPublicKeyPath)
	}

	router := chi.NewRouter()
	router.Use(authentication.GetToken())

	router.Route(cardtype.MainPath, cardtype.SubRoutes)
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
