package main

// ECDSApublicKeyXY : point of ellictic curve that is the same at public key ECDSA
type ECDSApublicKeyXY struct {
	X string `json:"x"`
	Y string `json:"y"`
}

// RSApublicKeyEN : numbers for public key of rsa type
type RSApublicKeyEN struct {
	E string `json:"e"`
	N string `json:"n"`
}
