package authentication

import (
	"context"
	"net/http"
	"strings"
)

func GetToken() func(next http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		fn := func(w http.ResponseWriter, r *http.Request) {
			auth := r.Header.Get("Authentication")
			if auth != "" {
				args := strings.Split(auth, " ")
				if len(args) == 2 && args[0] == "Bearer" {
					claims := ValidateToken(args[1])
					if claims == nil {
						http.Error(w, "Invalid token", 401)
						return
					}
					r = r.WithContext(context.WithValue(r.Context(), "auth", claims))
				} else {
					http.Error(w, "Expected, Authentication: Bearer <token>", 401)
					return
				}
			}
			next.ServeHTTP(w, r)
		}
		return http.HandlerFunc(fn)
	}
}
