package com.shoponline.warehouse.model;

import javax.persistence.*;
import java.io.Serializable;


@IdClass(ReservedItemID.class)
@Entity
public class ReservedItem implements Serializable {
    @Id
    private Integer itemId;
    @Id
    private Integer orderId;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public ReservedItemStatus getStatus() {
        return status;
    }

    public void setStatus(ReservedItemStatus status) {
        this.status = status;
    }

    private Integer amount;
    private ReservedItemStatus status;

    public ReservedItem() {}

    public ReservedItem(Integer itemId, Integer orderId, Integer amount) {
        this.itemId = itemId;
        this.orderId = orderId;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "{" +
                "itemId=" + itemId +
                ", orderId=" + orderId +
                ", amount=" + amount +
                ", status=" + status +
                '}';
    }
}

