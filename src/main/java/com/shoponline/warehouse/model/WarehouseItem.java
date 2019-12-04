package com.shoponline.warehouse.model;

import javax.persistence.*;

@Entity
public class WarehouseItem {

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    private @GeneratedValue @Id Long id;

    private Integer amount;
    private String name;
    private Integer price;

    public WarehouseItem() {}

    public WarehouseItem(String name, Integer price, Integer amount) {
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Item [name=" + name + ", id=" + id + ", amount=" + amount + "]";
    }

}
