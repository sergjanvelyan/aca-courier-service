package com.aca.acacourierservice.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String orderId;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @ManyToOne
    @JoinColumn(name = "courier_id")
    private User courier;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String zipCode;
    @Column(nullable = false)
    private String fullName;
    @Column(unique = true)
    private String trackingNumber;
    @Column(nullable = false)
    private double deliveryPrice;
    @Column(nullable = false)
    private double totalPrice;
    @Column(nullable = false)
    private double weightKg;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Size size;
    @Column(nullable = false)
    private LocalDateTime orderConfirmedTime;
    @Column
    private LocalDateTime orderDeliveredTime;
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<StatusUpdateTime> statusUpdateTimeList;
    @PrePersist
    public void generateTrackingNumber(){
        if(this.trackingNumber==null || this.trackingNumber.isEmpty()){
            this.trackingNumber= UUID.randomUUID().toString();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public User getCourier() {
        return courier;
    }

    public void setCourier(User courier) {
        this.courier = courier;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
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

    public double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
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

    public List<StatusUpdateTime> getStatusUpdateTimeList() {
        return statusUpdateTimeList;
    }

    public void setStatusUpdateTimeList(List<StatusUpdateTime> statusUpdateTimeList) {
        this.statusUpdateTimeList = statusUpdateTimeList;
    }

    public enum Status {
        NEW,
        SHIPPED,
        CANCELLED,
        DELIVERING,
        DELIVERED,
        POSTPONED
    }
    public enum Size {
        SMALL,
        MEDIUM,
        LARGE,
        EXTRA_LARGE
    }
}
