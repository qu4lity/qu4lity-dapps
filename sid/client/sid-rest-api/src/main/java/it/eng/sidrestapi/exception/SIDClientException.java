package it.eng.sidrestapi.exception;


public class SIDClientException extends Exception {

    public SIDClientException() {
        super();
    }

    public SIDClientException(String message) {
        super(message);
    }

    public SIDClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public SIDClientException(Throwable cause) {
        super(cause);
    }

    protected SIDClientException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }



}

