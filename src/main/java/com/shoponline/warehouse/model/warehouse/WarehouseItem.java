package com.shoponline.warehouse.model.warehouse;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class WarehouseItem {

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    private @GeneratedValue @Id Integer id;

    private Integer amount;
    private String name;
    private BigDecimal price;

    public WarehouseItem() {}

    public WarehouseItem(String name, BigDecimal price, Integer amount) {
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Item [name=" + name + ", id=" + id + ", amount=" + amount + "]";
    }

}
