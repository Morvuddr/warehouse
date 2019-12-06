package com.shoponline.warehouse.dtos;

import java.io.Serializable;

public class ChangeStatusDTO implements Serializable {

    public Integer OrderId;
    public String Status;

    public ChangeStatusDTO() {}

    public ChangeStatusDTO(Integer orderId, String status) {
        OrderId = orderId;
        Status = status;
    }

    @Override
    public String toString() {
        return "{" +
                "\"OrderId\":" + OrderId +
                ", \"Status\":\"" + Status + '\"' +
                '}';
    }
}
