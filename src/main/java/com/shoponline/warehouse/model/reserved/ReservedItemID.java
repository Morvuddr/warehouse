package com.shoponline.warehouse.model.reserved;


import java.io.Serializable;
import java.util.Objects;

public class ReservedItemID implements Serializable {

    private Integer itemId;
    private Integer orderId;

    public ReservedItemID(Integer itemId, Integer orderId) {
        this.itemId = itemId;
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservedItemID that = (ReservedItemID) o;
        return Objects.equals(itemId, that.itemId) &&
                Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, orderId);
    }

    @Override
    public String toString() {
        return "{" +
                "itemId=" + itemId +
                ", orderId=" + orderId +
                '}';
    }
}
