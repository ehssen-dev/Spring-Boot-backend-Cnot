package tn.pfe.CnotConnectV1.enums;

public enum OrderStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    DELIVERED("Delivered"),
    EMERGENCY("Emergency"),
    IN_PROGRESS("InProgress"),
    CANCELED("Cancelled"),
    COMPLETED("Completed"),
    OVERDUE("Overdue"),
    CRITICAL("Critical"),;


    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}