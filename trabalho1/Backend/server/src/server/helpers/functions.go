package helpers

import (
	"server/authentication"
	"net/http"
)

func GetAuth(r *http.Request) *authentication.MyClaims {
	claims := r.Context().Value("auth")
	if claims == nil {
		return nil
	}
	return claims.(*authentication.MyClaims)
}
