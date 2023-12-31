package com.aca.acacourierservice.model;

import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.validation.ValidEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class StatusInfoJson {
    @NotNull(message = "Please enter status")
    @ValidEnum(enumClass =Order.Status.class)
    @NotEmpty(message = "Invalid enum value")
    private String status;

    private String additionalInfo;


    public StatusInfoJson(String status, String additionalInfo) {
        this.status = status;
        this.additionalInfo = additionalInfo;
    }

    public Order.Status getStatus() {
        if(status==null){
            return null;
        }
        return Order.Status.valueOf(status.toUpperCase());
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
