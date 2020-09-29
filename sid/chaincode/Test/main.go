package main

import (
	"crypto/ecdsa"
	"crypto/elliptic"
	"crypto/sha256"
	"encoding/hex"

	"fmt"
	"math/big"

	"github.com/btcsuite/btcutil/base58"
)

// https://swenotes.wordpress.com/2018/04/16/trying-to-learn-ecdsa-and-golang/

func main() {
	// var message string = "publicKeyToCreate"

	// ECDSApublicKeyXY : point of ellictic curve that is the same at public key ECDSA
	type ECDSApublicKeyXY struct {
		X string `json:"x"`
		Y string `json:"y"`
	}

	var pk ECDSApublicKeyXY = ECDSApublicKeyXY{}

	pk.X = "308f694109a8b6a1780c9964ccdb48f7ea4d6708afeb7bfa5f8244903a497b76"
	pk.Y = "00aa5a4a34fe12e5c2c1e7d21894ad8f50b9030b2d3086c1a2d174fd8aa986959e"

	var publicKey *ecdsa.PublicKey
	publicKey = hexToPublicKey(pk.X, pk.Y)

	fmt.Println("public key ricalcolata: ", publicKey)

	/* 	rsaprivateKey, err := rsa.GenerateKey(rand.Reader, 2048)
	   	if err != nil {
	   		fmt.Printf("Error from privateKey: %s\n", err)
	   		return
	   	}
	   	rng := rand.Reader
	   	hashed := sha256.Sum256([]byte("ciao"))

	   	respString := base58.Encode([]byte(hashed[:]))
	   	// resp := base64.StdEncoding.EncodeToString(hashed[:])
	   	// respString := base58.Encode([]byte(resp))

	   	fmt.Println("RESPONSE ENCODING: ", respString)

	   	hashed = sha256.Sum256([]byte(message))
	   	signature, err := rsa.SignPKCS1v15(rng, rsaprivateKey, crypto.SHA256, hashed[:])
	   	if err != nil {
	   		fmt.Printf("Error from signing: %s\n", err)
	   		return
	   	}

	   	fmt.Printf("Signature: %x\n", signature)

	   	pk.E = rsaprivateKey.PublicKey.E
	   	pk.N = rsaprivateKey.PublicKey.N

	   	var rsapublicKey rsa.PublicKey

	   	rsapublicKey.E = rsaprivateKey.PublicKey.E
	   	rsapublicKey.N = rsaprivateKey.PublicKey.N

	   	hashed = sha256.Sum256([]byte(message))

	   	err = rsa.VerifyPKCS1v15(&rsapublicKey, crypto.SHA256, hashed[:], signature)

	   	if err != nil {
	   		fmt.Printf("Error from verify: %s\n", err)
	   		return
	   	} */

	fmt.Printf("Everythings is ok")

}

// createAddressFromPKeyBlob creates a Addess from pKeyBlob input parameters
func createAddressFromPKeyBlob(pKeyBlob string) string {
	byteAddrCalc32 := sha256.Sum256([]byte(pKeyBlob))
	fmt.Println(byteAddrCalc32)
	fmt.Println("base58: " + string(base58.Encode([]byte(byteAddrCalc32[:]))))
	return base58.Encode([]byte(byteAddrCalc32[:]))

}

func hexToPublicKey(xHex string, yHex string) (pb *ecdsa.PublicKey) {
	xBytes, _ := hex.DecodeString(xHex)
	x := new(big.Int)
	x.SetBytes(xBytes)

	yBytes, _ := hex.DecodeString(yHex)
	y := new(big.Int)
	y.SetBytes(yBytes)

	pub := new(ecdsa.PublicKey)
	pub.X = x
	pub.Y = y

	pub.Curve = elliptic.P256()

	return pub
}
