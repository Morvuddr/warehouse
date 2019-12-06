package com.shoponline.warehouse.model.reserved;

public enum ReservedItemStatus {
    COLLECTING,
    PAID,
    FAILED,
    CANCELLED,
    SHIPPING,
    COMPLETE;
    public static ReservedItemStatus getEnumByString(String str) {
        switch (str) {
            case "Collecting": {
                return ReservedItemStatus.COLLECTING;
            }
            case "Paid": {
                return ReservedItemStatus.PAID;
            }
            case "Failed": {
                return ReservedItemStatus.FAILED;
            }
            case "Cancelled": {
                return ReservedItemStatus.CANCELLED;
            }
            case "Shipping": {
                return ReservedItemStatus.SHIPPING;
            }
            case "Complete": {
                return ReservedItemStatus.COMPLETE;
            }
        }
        return null;
    }
}
