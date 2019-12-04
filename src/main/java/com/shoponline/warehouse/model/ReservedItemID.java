package com.shoponline.warehouse.model;


import java.io.Serializable;

public class ReservedItemID implements Serializable {

    private Integer itemId;
    private Integer orderId;

    public ReservedItemID(Integer itemId, Integer orderId) {
        this.itemId = itemId;
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "{" +
                "itemId=" + itemId +
                ", orderId=" + orderId +
                '}';
    }
}
