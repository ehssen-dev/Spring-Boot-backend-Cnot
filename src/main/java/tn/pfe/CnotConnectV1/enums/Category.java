package tn.pfe.CnotConnectV1.enums;

public enum Category {
    EQUIPMENT("Equipment"),
    SUPPLIES("Supplies"),
    SERVICES("Services"),
    TRAINING("Training"),
    TRAVEL("Travel"),
    MAINTENANCE("Maintenance"),
    CONSULTING("Consulting"),
    SOFTWARE("Software"),
    HARDWARE("Hardware"),
    CONTRACTS("Contracts"),
    OTHER("Other");

    private final String label;

    Category(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
