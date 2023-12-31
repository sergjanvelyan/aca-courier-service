package com.aca.acacourierservice.model;

import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.view.Public;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusUpdateTimeJson {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonView(Public.class)
    private LocalDateTime updateTime;
    @JsonView(Public.class)
    private Order.Status updatedFrom;
    @JsonView(Public.class)
    private Order.Status updatedTo;
    @JsonView(Public.class)
    private String additionalInfo;
    @JsonIgnore
    private Long orderId;

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Order.Status getUpdatedFrom() {
        return updatedFrom;
    }

    public void setUpdatedFrom(Order.Status updatedFrom) {
        this.updatedFrom = updatedFrom;
    }

    public Order.Status getUpdatedTo() {
        return updatedTo;
    }

    public void setUpdatedTo(Order.Status updatedTo) {
        this.updatedTo = updatedTo;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
