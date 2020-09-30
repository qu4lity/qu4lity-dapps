package main

// CryptedDataControl : Structure of data crypted from the client for the Chaincode
type CryptedDataControl struct {
	AddrControl string `json:"addrControl"`
	CryptoData  string `json:"cryptoData"`
	DataTime    string `json:"dataTime"`
}
