package main

// const: Entity of project for Key Manage
const (
	MESSAGE = "MESSAGE"
)

// const: ROLE e UID attribute
const (
	ROLE        = "role"
	UUID        = "uuid"
	DOMAIN      = "domain"
	ENVIRONMENT = "environment"
	PROCESS     = "process"
	NAME        = "name"
)

// const: ROLE 0 USER, ROLE 1 ADMIN, ROLE 2 CREATOR
const (
	READER = "reader"
	WRITER = "writer"
	ADMIN  = "admin"
)

// const: CHARACTER ALLOWED and Limit max for string and for url
const (
	ALPHACHAROK    = "abcdefghijklmnopqrstuvwxyz0123456789+-_@"
	LIMITMAXSTRING = 50
	LIMITMAXURL    = 256
)

// const: CHARACTER ALLOWED and Limit max for string and for url
const (
	ERROR   = "ERROR!!"
	WARNING = "WARNING"
	INFO    = "INFO***"
	DEBUG   = "DEBUG->"
)
