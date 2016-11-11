package keypair

import (
	"crypto"
	"crypto/rand"
	"crypto/rsa"
	_ "crypto/sha1"
	"crypto/x509"
	"encoding/pem"
	"errors"
	"io/ioutil"
	"os"
)

type Keypair struct {
	privateKey *rsa.PrivateKey
}

const hashAlgorithm = crypto.SHA1
const bits = 2048

func CreateKeypair() *Keypair {
	privateKey, err := rsa.GenerateKey(rand.Reader, bits)
	if err != nil {
		panic(err)
	}
	return &Keypair{privateKey}
}

func LoadKeypair(path string) *Keypair {
	bytes, err := ioutil.ReadFile(path)
	if err != nil {
		panic(err)
	}

	block, _ := pem.Decode(bytes)
	privateKey, err := x509.ParsePKCS1PrivateKey(block.Bytes)
	if err != nil {
		panic(err)
	}
	return &Keypair{privateKey}
}

func CreateKeypairIfNoExists(path string) (*Keypair, bool) {
	if finfo, err := os.Stat(path); err == nil {
		if finfo.IsDir() {
			panic(errors.New("keypair path must be a file"))
		}
		return LoadKeypair(path), true
	}
	return CreateKeypair(), false
}

func (keypair *Keypair) SaveKeypair(path string) {
	pemdata := pem.EncodeToMemory(
		&pem.Block{
			Type:  "RSA PRIVATE KEY",
			Bytes: x509.MarshalPKCS1PrivateKey(keypair.privateKey),
		},
	)
	err := ioutil.WriteFile(path, pemdata, 0600)
	if err != nil {
		panic(err)
	}
}

func (keypair *Keypair) ExportPublicKey(path string) {
	marshalledPublicKey, err := x509.MarshalPKIXPublicKey(&keypair.privateKey.PublicKey)
	if err != nil {
		panic(err)
	}

	pemdata := pem.EncodeToMemory(&pem.Block{
		Type:  "RSA PUBLIC KEY",
		Bytes: marshalledPublicKey,
	})

	err = ioutil.WriteFile(path, pemdata, 0600)
	if err != nil {
		panic(err)
	}
}

func (keypair *Keypair) Sign(msg []byte) ([]byte, error) {
	var opts rsa.PSSOptions
	opts.SaltLength = rsa.PSSSaltLengthAuto
	hash := hashAlgorithm.New()
	hash.Write(msg)
	hashed := hash.Sum(nil)
	signature, err := rsa.SignPSS(rand.Reader, keypair.privateKey, hashAlgorithm, hashed, &opts)
	if err != nil {
		return nil, err
	}
	return signature, nil
}
