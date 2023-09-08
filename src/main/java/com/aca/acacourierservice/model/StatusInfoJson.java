package com.aca.acacourierservice.model;

import com.aca.acacourierservice.entity.Order;

public class StatusInfoJson {
    private Order.Status status;
    private String additionalInfo;
    public StatusInfoJson() {
    }
    public StatusInfoJson(Order.Status status, String additionalInfo) {
        this.status = status;
        this.additionalInfo = additionalInfo;
    }

    public Order.Status getStatus() {
        return status;
    }

    public void setStatus(Order.Status status) {
        this.status = status;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
