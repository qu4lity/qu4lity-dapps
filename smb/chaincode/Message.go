package main

// Message : Manage Secure Messaging Board
type Message struct {
	PkPreamble      `json:"pkPreamble"`
	Name            string  `json:"name"`
	Version         int     `json:"version"`
	Created         string  `json:"created"`
	CreatedBy       string  `json:"createdBy"`
	SignedBy        string  `json:"signedBy"`
	Seal            string  `json:"seal"`
	ConfidentialFor string  `json:"confidentialFor"`
	MessageRef      string  `json:"messageRef"`
	MessageSize     float64 `json:"messageSize"`
}
