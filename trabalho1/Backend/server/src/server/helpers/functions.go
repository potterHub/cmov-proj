package helpers

import (
	"net/http"
	"server/authentication"
)

func GetAuth(r *http.Request) *authentication.MyClaims {
	claims := r.Context().Value("auth")
	if claims == nil {
		return nil
	}
	return claims.(*authentication.MyClaims)
}
