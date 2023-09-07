package com.aca.acacourierservice.model;

import com.aca.acacourierservice.entity.Order;
import java.time.LocalDateTime;

public class OrderJson {
    private String orderId;
    private String trackingNumber;
    private long storeId;
    private String fullName;
    private String country;
    private String city;
    private String address;
    private String phone;
    private String zipCode;
    private double weightKg;
    private Order.Size size;
    private long courierId;
    private double deliveryPrice;
    private double totalPrice;
    private Order.Status status;
    private LocalDateTime orderConfirmedTime;
    private LocalDateTime orderDeliveredTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }

    public Order.Size getSize() {
        return size;
    }

    public void setSize(Order.Size size) {
        this.size = size;
    }

    public long getCourierId() {
        return courierId;
    }

    public void setCourierId(long courierId) {
        this.courierId = courierId;
    }

    public double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Order.Status getStatus() {
        return status;
    }

    public void setStatus(Order.Status status) {
        this.status = status;
    }

    public LocalDateTime getOrderConfirmedTime() {
        return orderConfirmedTime;
    }

    public void setOrderConfirmedTime(LocalDateTime orderConfirmedTime) {
        this.orderConfirmedTime = orderConfirmedTime;
    }

    public LocalDateTime getOrderDeliveredTime() {
        return orderDeliveredTime;
    }

    public void setOrderDeliveredTime(LocalDateTime orderDeliveredTime) {
        this.orderDeliveredTime = orderDeliveredTime;
    }
}