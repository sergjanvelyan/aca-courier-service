package com.aca.acacourierservice.model;

import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.validationGroups.OnCreate;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;
public class OrderJson {
    @NotBlank
    private String orderId;
    @Null(groups = OnCreate.class,message = "Tracking number should be generated")
    private String trackingNumber;
    @Null(groups = OnCreate.class,message = "You don't need to enter store id")
    @Positive
    private Long storeId;
    @Pattern(regexp ="^[a-zA-Z]{2,30}$",message = "Not valid name")
    private String fullName;
    @Pattern(regexp ="^[a-zA-Z]{2,15}$",message = "Not valid country")
    private String country;
    @Pattern(regexp ="^[a-zA-Z]{2,15}$",message = "Not valid city")
    private String city;
    @Pattern(regexp = "^([a-zA-Z]{2,15}|[1-9]+)\\s([1-9]+|([1-9]+/[1-9]+))$",message = "Not valid address")
    private String address;
    @Pattern(regexp ="^[+][0-9]{10,15}$",message = "Phone number should be like +{country code}{phone number}")
    private String phone;
    @Pattern(regexp ="^[A-Z0-9]{2,15}$",message = "Not valid zip code")
    private String zipCode;
    @Positive
    private Double weightKg;
    private Order.Size size;
    private long courierId;
    @Positive
    private Double deliveryPrice;
    @Positive
    private Double totalPrice;
    private Order.Status status;
    private LocalDateTime orderConfirmedTime;
    private LocalDateTime orderDeliveredTime;
    private List<StatusUpdateTimeJson> statusUpdateHistory;

    public List<StatusUpdateTimeJson> getStatusUpdateHistory() {
        return statusUpdateHistory;
    }

    public void setStatusUpdateHistory(List<StatusUpdateTimeJson> statusUpdateHistory) {
        this.statusUpdateHistory = statusUpdateHistory;
    }

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

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
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

    public Double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(Double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
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