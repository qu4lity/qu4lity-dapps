package main

// PkPreamble : Composite Key
type PkPreamble struct {
	Domain      string `json:"domain"`      // (PK)
	Environment string `json:"environment"` // (PK)
	Process     string `json:"process"`     // (PK)
}
