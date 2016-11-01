package cardTypes

import (
	"net/http"
	"server/globals"
	"encoding/json"
)

func getCreditCardTypes(w http.ResponseWriter, r *http.Request) {
	creditCardTypes, err := globals.DB.GetCreditCardTypes()
	if err != nil {
		http.Error(w, "Failed retrieving creditCardTypes", 500)
		return
	}

	creditCardTypesSlice, err := json.Marshal(creditCardTypes)
	if err != nil {
		http.Error(w, "Failed converting creditCardTypes to JSON", 500)
		return
	}
	w.Write(creditCardTypesSlice)
}