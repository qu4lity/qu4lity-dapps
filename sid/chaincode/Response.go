package main

import (
	"encoding/json"
)

// Response : Code Response
type Response struct {
	Code    int    `json:"code"`
	Message string `json:"message"`
}


// StringResp method for stringify the Json data
func StringResp(resp Response) string {

	var respByte []byte
	var err error
	respByte, err = json.Marshal(resp)
	if err != nil {
		return "500 - Error in Marshal of Response "
	}
	return string(respByte)
}

// ValResp method for stringify the Json data with input parameters
func ValResp(code int, msg string) string {
	var resp Response = Response{}
	resp.Code = code
	resp.Message = msg
	return StringResp(resp)
}