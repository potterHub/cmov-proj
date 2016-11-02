package db

import (
	"bufio"
	"encoding/base64"
	"os"
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
