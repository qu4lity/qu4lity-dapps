package main

// IdentityMessage is a structure for CA Identity Attributes value by Message
type IdentityMessage struct {
	Client       string   `json:"client"`
	Role         string   `json:"role"`
	Domains      []string `json:"domains"`
	Environments []string `json:"environments"`
	Processes    []string `json:"processes"`
	Names        []string `json:"names"`
}
