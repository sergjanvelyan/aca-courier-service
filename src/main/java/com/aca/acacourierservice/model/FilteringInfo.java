package com.aca.acacourierservice.model;

import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.validation.ValidEnum;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class FilteringInfo {
    @Pattern(regexp = "^(([a-zA-Z]{2,15})\\s?)+$", message = "Not valid country")
    private String country;
    @Pattern(regexp = "^(([a-zA-Z]{2,15})\\s?)+$", message = "Not valid city")
    private String city;
    @Pattern(regexp = "^[A-Z0-9]{2,15}$", message = "Not valid zip code")
    private String zipCode;
    @ValidEnum(enumClass = Order.Status.class)
    private String status;
    @Positive
    private Double deliveryPriceMin;
    @Positive
    private Double deliveryPriceMax;
    @Positive
    private Double totalPriceMin;
    @Positive
    private Double totalPriceMax;
    @Positive
    private Double weightMin;
    @Positive
    private Double weightMax;
    @ValidEnum(enumClass = Order.Size.class)
    private String size;
    private LocalDateTime orderConfirmedTimeMin;
    private LocalDateTime orderConfirmedTimeMax;
    private LocalDateTime orderDeliveredTimeMin;
    private LocalDateTime orderDeliveredTimeMax;

    public Order.Status getStatus() {
        if(status==null){
            return null;
        }
        return Order.Status.valueOf(status.toUpperCase());
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Double getDeliveryPriceMin() {
        return deliveryPriceMin;
    }

    public void setDeliveryPriceMin(Double deliveryPriceMin) {
        this.deliveryPriceMin = deliveryPriceMin;
    }

    public Double getDeliveryPriceMax() {
        return deliveryPriceMax;
    }

    public void setDeliveryPriceMax(Double deliveryPriceMax) {
        this.deliveryPriceMax = deliveryPriceMax;
    }

    public Double getTotalPriceMin() {
        return totalPriceMin;
    }

    public void setTotalPriceMin(Double totalPriceMin) {
        this.totalPriceMin = totalPriceMin;
    }

    public Double getTotalPriceMax() {
        return totalPriceMax;
    }

    public void setTotalPriceMax(Double totalPriceMax) {
        this.totalPriceMax = totalPriceMax;
    }

    public Double getWeightMin() {
        return weightMin;
    }

    public void setWeightMin(Double weightMin) {
        this.weightMin = weightMin;
    }

    public Double getWeightMax() {
        return weightMax;
    }

    public void setWeightMax(Double weightMax) {
        this.weightMax = weightMax;
    }

    public Order.Size getSize() {
        if(size==null){
            return null;
        }
        return Order.Size.valueOf(size.toUpperCase());
    }

    public void setSize(String size) {
        this.size = size;
    }

    public LocalDateTime getOrderConfirmedTimeMin() {
        return orderConfirmedTimeMin;
    }

    public void setOrderConfirmedTimeMin(LocalDateTime orderConfirmedTimeMin) {
        this.orderConfirmedTimeMin = orderConfirmedTimeMin;
    }

    public LocalDateTime getOrderConfirmedTimeMax() {
        return orderConfirmedTimeMax;
    }

    public void setOrderConfirmedTimeMax(LocalDateTime orderConfirmedTimeMax) {
        this.orderConfirmedTimeMax = orderConfirmedTimeMax;
    }

    public LocalDateTime getOrderDeliveredTimeMin() {
        return orderDeliveredTimeMin;
    }

    public void setOrderDeliveredTimeMin(LocalDateTime orderDeliveredTimeMin) {
        this.orderDeliveredTimeMin = orderDeliveredTimeMin;
    }

    public LocalDateTime getOrderDeliveredTimeMax() {
        return orderDeliveredTimeMax;
    }

    public void setOrderDeliveredTimeMax(LocalDateTime orderDeliveredTimeMax) {
        this.orderDeliveredTimeMax = orderDeliveredTimeMax;
    }
}