package com.aca.acacourierservice.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class StatusUpdateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private LocalDateTime updateTime;
    @Column
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusUpdateTime that = (StatusUpdateTime) o;
        return id == that.id
                && Objects.equals(updateTime, that.updateTime)
                && updatedFrom == that.updatedFrom
                && updatedTo == that.updatedTo
                && Objects.equals(additionalInfo, that.additionalInfo)
                && Objects.equals(order, that.order);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id,
                updateTime,
                updatedFrom,
                updatedTo,
                additionalInfo,
                order);
    }
}