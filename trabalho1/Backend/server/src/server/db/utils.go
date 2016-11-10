package db

import (
	"bufio"
	"encoding/base64"
	"math/rand"
	"os"
	"server/globals/keys"
)

func encodeFileToBase64(filePath string) (string, error) {
	imageFile, err := os.Open(filePath)
	if err != nil {
		return "", err
	}

	defer imageFile.Close()
	imageFileInfo, _ := imageFile.Stat()
	imageBuffer := make([]byte, imageFileInfo.Size())
	imageReader := bufio.NewReader(imageFile)
	imageReader.Read(imageBuffer)

	return base64.StdEncoding.EncodeToString(imageBuffer), nil
}

func generateVoucherCode() (string, error) {
	voucherCode := RandStringBytesMaskImpr(20)
	signature, err := keys.Server.Sign([]byte(voucherCode))
	if err != nil {
		return "", err
	}
	return voucherCode + "." + base64.StdEncoding.EncodeToString(signature), nil
}

// http://stackoverflow.com/questions/22892120/how-to-generate-a-random-string-of-a-fixed-length-in-golang
const letterBytes = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
const (
	letterIdxBits = 6                    // 6 bits to represent a letter index
	letterIdxMask = 1<<letterIdxBits - 1 // All 1-bits, as many as letterIdxBits
	letterIdxMax  = 63 / letterIdxBits   // # of letter indices fitting in 63 bits
)

func RandStringBytesMaskImpr(n int) string {
	b := make([]byte, n)
	for i, cache, remain := n-1, rand.Int63(), letterIdxMax; i >= 0; {
		if remain == 0 {
			cache, remain = rand.Int63(), letterIdxMax
		}
		if idx := int(cache & letterIdxMask); idx < len(letterBytes) {
			b[i] = letterBytes[idx]
			i--
		}
		cache >>= letterIdxBits
		remain--
	}

	return string(b)
}
