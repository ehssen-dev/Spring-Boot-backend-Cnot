package tn.pfe.CnotConnectV1.exeptions;

public class MailException extends RuntimeException {

    public MailException(String message) {
        super(message);
    }

    public MailException(String message, Throwable cause) {
        super(message, cause);
    }
}
