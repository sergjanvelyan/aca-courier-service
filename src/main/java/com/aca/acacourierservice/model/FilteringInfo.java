package com.aca.acacourierservice.model;

import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.validation.ValidEnum;
import com.aca.acacourierservice.validation.ValidLocalDateTime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FilteringInfo {
    private String country;
    private String city;
    private String zipCode;
    @ValidEnum(enumClass = Order.Status.class)
    private String status;
    private Double deliveryPriceMin;
    private Double deliveryPriceMax;
    private Double totalPriceMin;
    private Double totalPriceMax;
    private Double weightMin;
    private Double weightMax;
    @ValidEnum(enumClass = Order.Size.class)
    private String size;
    @ValidLocalDateTime
    private String orderConfirmedTimeMin;
    @ValidLocalDateTime
    private String orderConfirmedTimeMax;
    @ValidLocalDateTime
    private String orderDeliveredTimeMin;
    @ValidLocalDateTime
    private String orderDeliveredTimeMax;

    public Order.Status getStatus() {
        if(status==null||status.isEmpty()){
            return null;
        }
        return Order.Status.valueOf(status.toUpperCase());
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCountry() {
        if(country.isEmpty()){
            return null;
        }
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        if(city.isEmpty()){
            return null;
        }
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        if(zipCode.isEmpty()){
            return null;
        }
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
        if(size==null||size.isEmpty()){
            return null;
        }
        return Order.Size.valueOf(size.toUpperCase());
    }

    public void setSize(String size) {
        this.size = size;
    }

    public LocalDateTime getOrderConfirmedTimeMin() {
        if(orderConfirmedTimeMin==null||orderConfirmedTimeMin.isEmpty()){
            return null;
        }
        return localDateTimeParser(orderConfirmedTimeMin);
    }
    public void setOrderConfirmedTimeMin(String orderConfirmedTimeMin) {
        this.orderConfirmedTimeMin = orderConfirmedTimeMin;
    }

    public LocalDateTime getOrderConfirmedTimeMax() {
        if(orderConfirmedTimeMax==null||orderConfirmedTimeMax.isEmpty()){
            return null;
        }
        return localDateTimeParser(orderConfirmedTimeMax);
    }

    public void setOrderConfirmedTimeMax(String orderConfirmedTimeMax) {
        this.orderConfirmedTimeMax = orderConfirmedTimeMax;
    }

    public LocalDateTime getOrderDeliveredTimeMin() {
        if(orderDeliveredTimeMin==null||orderDeliveredTimeMin.isEmpty()){
            return null;
        }
        return localDateTimeParser(orderDeliveredTimeMin);
    }

    public void setOrderDeliveredTimeMin(String orderDeliveredTimeMin) {
        this.orderDeliveredTimeMin = orderDeliveredTimeMin;
    }

    public LocalDateTime getOrderDeliveredTimeMax() {
        if(orderDeliveredTimeMax==null||orderDeliveredTimeMax.isEmpty()){
            return null;
        }
        return localDateTimeParser(orderDeliveredTimeMax);
    }

    public void setOrderDeliveredTimeMax(String orderDeliveredTimeMax) {
        this.orderDeliveredTimeMax = orderDeliveredTimeMax;
    }

    public LocalDateTime localDateTimeParser(String localDateTime) throws IllegalArgumentException{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        try{
            return LocalDateTime.parse(localDateTime,formatter);
        }catch (DateTimeParseException e){
            throw new IllegalArgumentException("Invalid date format: " + localDateTime);
        }
    }
}