package main

// StatusEntry : StatusEntry second part of Identity
type StatusEntry struct {
	Address    string `json:"address"`    // (PK
	SubAddress string `json:"subAddress"` // (PK)
	ValidFrom  string `json:"validFrom"`
	UpdatedBy  string `json:"updatedBy"`
	Status     int    `json:"status"` // 1: ACTIVE, 2: SUSPENDED, 3: REVOKED
}
