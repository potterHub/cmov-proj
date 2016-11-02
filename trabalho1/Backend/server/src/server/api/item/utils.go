package item

import (
	"crypto/sha256"
	"encoding/base32"
)

func buildResponse(byteArray []byte, hashQuery string, resourceKeyName string) []byte {
	verifyHashSha256 := func(byteArray []byte, checksum string) (bool, string) {
		binaryHash := sha256.Sum256(byteArray)
		encodedHash := base32.StdEncoding.EncodeToString(binaryHash[:])
		if checksum == encodedHash {
			return true, checksum
		}
		return false, encodedHash
	}
	same, hash := verifyHashSha256(byteArray, hashQuery)
	response := `{"hash":"` + hash + `"}`
	if !same {
		response = `{"hash":"` + hash + `","` + resourceKeyName + `":` + string(byteArray) + `}`
	}
	return []byte(response)
}
