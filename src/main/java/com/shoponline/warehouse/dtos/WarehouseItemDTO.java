package com.shoponline.warehouse.dtos;

import java.io.Serializable;
import java.math.BigDecimal;

public class WarehouseItemDTO implements Serializable {
    public Integer itemId;
    public Integer amount;
    public String name;
    public BigDecimal price;

    public WarehouseItemDTO () {}

    public WarehouseItemDTO(Integer itemId, Integer amount, String name, BigDecimal price) {
        this.itemId = itemId;
        this.amount = amount;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "{" +
                "\"ItemId\":" + itemId +
                ", \"Amount\":" + amount +
                ", \"Name:\":\"" + name + '\"' +
                ", \"Price\":" + price +
                '}';
    }
}
