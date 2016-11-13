package helpers

import (
	"math"
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

/** http://stackoverflow.com/questions/18390266/how-can-we-truncate-float64-type-to-a-particular-precision-in-golang **/
func round(num float64) int {
	return int(num + math.Copysign(0.5, num))
}
func RoundFloat(num float64, precision int) float64 {
	output := math.Pow(10, float64(precision))
	return float64(round(num*output)) / output
}

/**/
