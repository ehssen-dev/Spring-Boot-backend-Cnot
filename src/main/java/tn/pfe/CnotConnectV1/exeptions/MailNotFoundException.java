package tn.pfe.CnotConnectV1.exeptions;

public class MailNotFoundException extends RuntimeException {

    public MailNotFoundException(String message) {
        super(message);
    }

    public MailNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}