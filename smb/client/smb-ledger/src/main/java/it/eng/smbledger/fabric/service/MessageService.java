package it.eng.smbledger.fabric.service;


import it.eng.smbledger.exception.FLedgerClientException;
import it.eng.smbledger.fabric.model.Message;

import java.math.BigDecimal;
import java.net.URL;

public interface MessageService {

    String postMessage(String domain, String environment, String process, String name, URL messageRef, BigDecimal messageSize,
                       String seal, URL signedBy, URL confidentialFor) throws FLedgerClientException;

    Message getMessage(String domain, String environment, String process, String name, Integer version) throws FLedgerClientException;

    Integer getLatestVersion(String domain, String environment, String process, String name) throws FLedgerClientException;


}
