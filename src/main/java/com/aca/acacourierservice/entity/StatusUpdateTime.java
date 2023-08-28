package com.aca.acacourierservice.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class StatusUpdateTime {
    @Id
    private long id;
    @Column(nullable = false)
    private Date updateTime;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Order.Status updatedFrom;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Order.Status updatedTo;
    @Column(name = "additional_info")
    private String additionalInfo;
    @ManyToOne
    @JoinColumn(name = "order_id",referencedColumnName = "id")
    private Order order;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}