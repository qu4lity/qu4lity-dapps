package main

// const: STATUS 1 ACTIVE, 2 SUSPENDED, 2 REVOKED
const (
	SUSPENDSTATUS = 2
	ACTIVESTATUS  = 1
	REVOKESTATUS  = 3
)

// const: Entity of project for Key Manage
const (
	STATUSENTRY        = "STATUSENTRY"
	BASEENTRY          = "BASEENTRY"
	IDENTITY           = "IDENTITY"
	MESSAGE            = "MESSAGE"
	STATE              = "STATE"
	OBJECTDISTRIBUTION = "OBJECTDISTRIBUTION"
)

// const: ROLE e UID attribute
const (
	ROLE = "role"
	UUID = "uuid"
	ADDR = "addr"
)

// const: ECDSA e RSA algotithm for Public Key
const (
	ECDSA = "ECDSA"
	RSA   = "RSA"
)

// const: ROLE 0 USER, ROLE 1 ADMIN, ROLE 2 CREATOR
const (
	USER    = "user"
	ADMIN   = "admin"
	CREATOR = "creator"
)

// const: Max N umber of Generations SID - Controller
const (
	NUMMAXGEN = 5
)

// const: CHARACTER ALLOWED and Limit max for string and for url
const (
	ERROR   = "ERROR!!"
	WARNING = "WARNING"
	INFO    = "INFO***"
	DEBUG   = "DEBUG->"
)
