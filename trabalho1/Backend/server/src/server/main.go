package main

import (
	"github.com/pressly/chi"
	"net/http"
	"os"
	"path/filepath"
	"server/api/item"
	"server/api/terminal"
	"server/api/user"
	"server/db"
	"server/globals"
)

const sqlite3DbPath = "../../sqlite/app.sqlite3"

func main() {
	cdToBinary();

	db := db.Connect(sqlite3DbPath);
	defer db.Close()
	globals.DB = db

	router := chi.NewRouter()

	router.Route(user.MainPath, user.SubRoutes)
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