package it.eng.smbledger.exception;


public class FLedgerClientException extends Exception {

    public FLedgerClientException() {
        super();
    }

    public FLedgerClientException(String message) {
        super(message);
    }

    public FLedgerClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public FLedgerClientException(Throwable cause) {
        super(cause);
    }

    protected FLedgerClientException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }



}

