package authentication

import (
	"fmt"
	"github.com/dgrijalva/jwt-go"
	"server/models"
	"time"
)

var jwtSimmetricKey = []byte("<j]A0JT8}*/|?0PJ{HE1BCa8Ss1&^q=$6M@Mu70m2*0m;8]!>l0JpCpexS2XK`]")

const expirationDelta = time.Hour * 24 * 60

type MyClaims struct {
	IdCustomer int64
	Name       string
	Username   string
	jwt.StandardClaims
}

func CreateToken(customer *models.Customer) (string, error) {
	claims := MyClaims{
		customer.IdCustomer,
		customer.Name,
		customer.Username,
		jwt.StandardClaims{
			ExpiresAt: time.Now().Add(expirationDelta).Unix(),
		},
	}
	token := jwt.NewWithClaims(jwt.SigningMethodHS512, claims)
	tokenString, err := token.SignedString(jwtSimmetricKey)
	if err != nil {
		return "", err
	}
	return tokenString, nil
}

func ValidateToken(tokenString string) *MyClaims {
	token, err := jwt.ParseWithClaims(tokenString, &MyClaims{},
		func(token *jwt.Token) (interface{}, error) {
			if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok || token.Header["alg"] != "HS512" {
				return nil, fmt.Errorf("Unexpected signing method: %v", token.Header["alg"])
			}
			return jwtSimmetricKey, nil
		})
	if err != nil {
		return nil
	}

	if claims, ok := token.Claims.(*MyClaims); ok && token.Valid {
		return claims
	}

	return nil
}
