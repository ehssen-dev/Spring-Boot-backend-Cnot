package tn.pfe.CnotConnectV1.exeptions;

public class ProcurementRequestNotFoundException extends RuntimeException {
    public ProcurementRequestNotFoundException(String message) {
        super(message);
    }
}
