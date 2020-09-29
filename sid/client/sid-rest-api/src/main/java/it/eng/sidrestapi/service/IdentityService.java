package it.eng.sidrestapi.service;


import it.eng.sidrestapi.exception.SIDClientException;
import it.eng.sidrestapi.model.Identity;
import it.eng.sidrestapi.model.Response;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

public interface IdentityService {

//    String postIdentity(String name, PublicKey pKeyBlob, String keyType, boolean active, String details) throws FLedgerClientException;

    Response postIdentity(PrivateKey privKeyController, PublicKey pKeyBlob, String keyType, boolean active, String details, String controller) ;

    Response suspendIdentity(String addr, String guid, String controller, PrivateKey privKeyController) throws SIDClientException;

    Response activateIdentity(String addr, String guid, String controller, PrivateKey privKeyController) throws SIDClientException;

    Response revokeIdentity(String addr, String guid, String controller, PrivateKey privKeyController) throws SIDClientException;

    Response getIdentity(String addr, Date date) throws SIDClientException;

}
