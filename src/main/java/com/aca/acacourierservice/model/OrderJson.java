package com.aca.acacourierservice.model;

import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.validation.OnCreate;
import com.aca.acacourierservice.validation.ValidEnum;
import com.aca.acacourierservice.view.Lists;
import com.aca.acacourierservice.view.PrivateSecondLevel;
import com.aca.acacourierservice.view.Public;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderJson {
    @JsonView(Lists.class)
    private Long id;
    @JsonView(PrivateSecondLevel.class)
    @NotBlank
    private String orderId;
    @JsonView(PrivateSecondLevel.class)
    @Null(groups = OnCreate.class,message = "Tracking number should be generated")
    private String trackingNumber;
    @JsonView(Public.class)
    @Null(groups = OnCreate.class,message = "You don't need to enter store id")
    @Positive
    private Long storeId;
    @JsonView(PrivateSecondLevel.class)
    @NotNull(groups = OnCreate.class,  message = "Please enter full name")
    @Pattern(regexp = "^(([a-zA-Z]{2,15})\\s?)+$",message = "Not valid name")
    private String fullName;
    @JsonView(Public.class)
    @NotNull(groups = OnCreate.class,  message = "Please enter country")
    @Pattern(regexp ="^(([a-zA-Z]{2,15})\\s?)+$",message = "Not valid country")
    private String country;
    @JsonView(Public.class)
    @NotNull(groups = OnCreate.class,  message = "Please enter city")
    @Pattern(regexp ="^(([a-zA-Z]{2,15})\\s?)+$",message = "Not valid city")
    private String city;
    @JsonView(PrivateSecondLevel.class)
    @NotNull(groups = OnCreate.class,  message = "Please enter address")
    @Pattern(regexp = "^(([a-zA-Z]{2,15}\\s?)+|[1-9][0-9]{0,5})\\s([1-9][0-9]{0,5}[a-zA-Z]?|([1-9][0-9]{0,5}/[1-9][0-9]{0,5}))$",message = "Not valid address")
    private String address;
    @JsonView(PrivateSecondLevel.class)
    @NotNull(groups = OnCreate.class,  message = "Please enter phone number")
    @Pattern(regexp ="^[+][0-9]{10,15}$",message = "Phone number should be like +{country code}{phone number}")
    private String phone;
    @JsonView(Public.class)
    @NotNull(groups = OnCreate.class,  message = "Please enter zipcode")
    @Pattern(regexp ="^[A-Z0-9]{2,15}$",message = "Not valid zip code")
    private String zipCode;
    @JsonView(PrivateSecondLevel.class)
    @NotNull(groups = OnCreate.class,  message = "Please enter weight in kgs")
    @Positive
    private Double weightKg;
    @JsonView(PrivateSecondLevel.class)
    @NotNull(groups = OnCreate.class,  message = "Please enter size")
    @ValidEnum(enumClass = Order.Size.class)
    private String size;
    @JsonView(PrivateSecondLevel.class)
    private Long courierId;
    @JsonView(PrivateSecondLevel.class)
    @NotNull(groups = OnCreate.class,  message = "Please enter calculated delivery price")
    @Positive
    private Double deliveryPrice;
    @JsonView(Public.class)
    @NotNull(groups = OnCreate.class,  message = "Please enter total price")
    @Positive
    private Double totalPrice;
    @JsonView(Public.class)
    @Null(groups = OnCreate.class,message = "You don't need to enter status.When creating order the status is 'NEW' by default")
    @ValidEnum(enumClass = Order.Status.class)
    private String status;
    @JsonView(PrivateSecondLevel.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderConfirmedTime;
    @JsonView(PrivateSecondLevel.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDeliveredTime;
    @JsonView(PrivateSecondLevel.class)
    private List<StatusUpdateTimeJson> statusUpdateHistory;

    public List<StatusUpdateTimeJson> getStatusUpdateHistory() {
        return statusUpdateHistory;
    }

    public void setStatusUpdateHistory(List<StatusUpdateTimeJson> statusUpdateHistory) {
        this.statusUpdateHistory = statusUpdateHistory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getStoreId() {
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
        if(size==null||size.isEmpty()){
            return null;
        }
        return Order.Size.valueOf(size.toUpperCase());
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getCourierId() {
        return courierId;
    }

    public void setCourierId(Long courierId) {
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
        if(status==null||status.isEmpty()){
            return null;
        }
        return Order.Status.valueOf(status.toUpperCase());
    }

    public void setStatus(String status) {
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