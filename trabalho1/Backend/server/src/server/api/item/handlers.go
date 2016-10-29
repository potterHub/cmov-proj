package item

import (
	"net/http"
	"server/globals"
	"encoding/json"
	"github.com/pressly/chi"
)

func getItems(w http.ResponseWriter, r *http.Request) {
	items , err := globals.DB.GetItems()
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
	itemsSlice, err := json.Marshal(items)
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
	w.Write(itemsSlice)
}

func getItem(w http.ResponseWriter, r *http.Request) {
	item, err := globals.DB.GetItem(chi.URLParam(r, "id"))
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
	itemSlice, err := json.Marshal(item)
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
	w.Write(itemSlice)
}