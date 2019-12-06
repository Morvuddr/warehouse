package com.shoponline.warehouse.model.reserved;

import javax.persistence.*;
import java.io.Serializable;


@IdClass(ReservedItemID.class)
@Entity
public class ReservedItem implements Serializable {

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

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

    @Id
    private Integer itemId;
    @Id
    private Integer orderId;
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

