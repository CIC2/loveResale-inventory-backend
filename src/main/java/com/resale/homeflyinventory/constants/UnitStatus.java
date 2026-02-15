package com.resale.homeflyinventory.constants;

public final class UnitStatus {
    private UnitStatus() {}

    public static final String AVAILABLE = "AV_N";
    public static final String SOLD = "H_NR";
    public static final String LOCKED = "T_L";
    public static final String IN_PROCESS = "T_LN";

    public static String getStatusDescription(String status) {
        if (status == null) return "Unknown";
        return switch (status) {
            case AVAILABLE -> "Available";
            case SOLD -> "Sold";
            case LOCKED -> "Locked";
            case IN_PROCESS -> "In Process";
            default -> "Unknown";
        };
    }
}


