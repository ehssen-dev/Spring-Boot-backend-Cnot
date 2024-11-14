package tn.pfe.CnotConnectV1.exeptions;

public class BudgetAllocationNotFoundException extends RuntimeException {

    public BudgetAllocationNotFoundException(String message) {
        super(message);
    }
}