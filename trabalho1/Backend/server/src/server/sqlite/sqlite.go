package sqlite

import (
	"database/sql"
	_ "github.com/mattn/go-sqlite3"
)

type Sqlite struct {
	db *sql.DB
}

func Connect(path string) *Sqlite {
	raw_db, err := sql.Open("sqlite3", path)
	if err != nil {
		panic(err)
	}

	return &Sqlite{db:raw_db}
}

func (* Sqlite) Close() {
	err := Sqlite.db.Close()
	if err != nil {
		panic(err)
	}
	Sqlite.db = nil
}
