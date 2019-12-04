package com.shoponline.warehouse.dtos;

import java.io.Serializable;

public class ReservedItemDTO implements Serializable {
    public Integer ItemId;
    public Integer OrderId;
    public Integer Amount;

    public ReservedItemDTO() {}

    public ReservedItemDTO(Integer itemId, Integer orderId, Integer amount) {
        ItemId = itemId;
        OrderId = orderId;
        Amount = amount;
    }

    @Override
    public String toString() {
        return "{" +
                "\"ItemId\":" + ItemId +
                ", \"OrderId\":" + OrderId +
                ", \"Amount\":" + Amount +
                '}';
    }
}
