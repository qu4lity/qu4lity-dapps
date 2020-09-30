package main

// BaseEntry : First part of Identity
type BaseEntry struct {
	Address    string `json:"address"`
	Created    string `json:"created"`
	CreatedBy  string `json:"createdBy"`
	Controller string `json:"controller"`
	PKeyBlob   string `json:"pKeyBlob"`
	KeyType    string `json:"keyType"`
	Details    string `json:"details"`
}
