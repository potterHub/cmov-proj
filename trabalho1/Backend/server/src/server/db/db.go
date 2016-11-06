package db

import (
	"database/sql"
	_ "github.com/mattn/go-sqlite3"
)

type DB struct {
	raw *sql.DB
}

func Connect(path string) *DB {
	raw_db, err := sql.Open("sqlite3", path)
	if err != nil {
		panic(err)
	}
	return &DB{raw: raw_db}
}

func (db *DB) Close() {
	err := db.raw.Close()
	if err != nil {
		panic(err)
	}
	db.raw = nil
}
