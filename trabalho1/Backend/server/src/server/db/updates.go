package db

func (db *DB) Blacklist(idCustomer int64) error {
	_, err := db.raw.Exec(`
	UPDATE customer
	SET blacklisted = 1
	WHERE idCustomer = ?`, idCustomer)
	return err
}
