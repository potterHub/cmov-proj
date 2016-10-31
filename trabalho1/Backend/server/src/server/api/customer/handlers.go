package customer

import (
	"net/http"
	"server/helpers"
	"encoding/json"
	"server/models"
	"server/globals"
	"server/authentication"
	"math/rand"
	"strconv"
	"database/sql"
	"github.com/elithrar/simple-scrypt"
	"unicode/utf8"
	"time"
)

// TODO: Missing credit card code checking
func registerCustomer(w http.ResponseWriter, r *http.Request) {
	claims := helpers.GetAuth(r)
	if claims != nil {
		http.Error(w, "Cannot register an authenticated customer", 400)
		return
	}

	// Decoding Input
	decoder := json.NewDecoder(r.Body)
	var newCustomer models.Customer
	err := decoder.Decode(&newCustomer)
	if err != nil {
		http.Error(w, "Invalid JSON", 400)
		return
	}

	// Check if all fields are set and correct
	error := ""
	if !(utf8.RuneCountInString(newCustomer.Name) >= 4) {
		error += "name>=4 "
	}
	if !(utf8.RuneCountInString(newCustomer.Username) >= 4) {
		error += "username>=4 "
	}
	if !(utf8.RuneCountInString(newCustomer.Password) >= 6) {
		error += "password>=6 "
	}
	if !(newCustomer.CreditCard.Month > 0 && newCustomer.CreditCard.Month <= 12) {
		error += "0<creditcard.month<=12 "
	}
	year := time.Now().Year()
	if !(newCustomer.CreditCard.Year >= year) {
		error += "creditcard.year>=" + strconv.Itoa(year)
	}
	if error != "" {
		http.Error(w, error, 400)
		return
	}

	// Check if username is already registered
	_, err = globals.DB.GetCustomer(newCustomer.Username)
	if err != sql.ErrNoRows {
		http.Error(w, "A customer already exists with that username", 400)
		return
	}

	// Hash the password
	hashedPassword, err := scrypt.GenerateFromPassword([]byte(newCustomer.Password), scrypt.DefaultParams)
	if err != nil {
		http.Error(w, "Failed to hash and salt password", 500)
		return
	}
	newCustomer.Password = string(hashedPassword)

	// Assign a 4 digit random PIN
	newCustomer.PIN = rand.Intn(1000) + 1000

	// Insert the new customer
	err = globals.DB.InsertCustomer(&newCustomer)
	if err != nil {
		http.Error(w, "Failed inserting new Customer", 400)
		return
	}

	// Create a token to be used in later authenticated requests
	// Should come in the HTML Header, Authentication: Bearer <token>
	tokenString, err := authentication.CreateToken(newCustomer)
	if err != nil {
		http.Error(w, "Failed to create token", 500)
		return
	}

	// Replies with JSON with token and PIN
	response := []byte(`{"token":"` + tokenString + `","PIN":` + strconv.Itoa(newCustomer.PIN) + "}")
	w.Write(response)
}

func loginCustomer(w http.ResponseWriter, r *http.Request) {
	claims := helpers.GetAuth(r)
	if claims != nil {
		http.Error(w, "Already authenticated", 400)
		return
	}
}

func getCustomer(w http.ResponseWriter, r *http.Request) {

}


func getCustomerVouchers(w http.ResponseWriter, r *http.Request) {

}

func getCustomerOrders(w http.ResponseWriter, r *http.Request) {

}
