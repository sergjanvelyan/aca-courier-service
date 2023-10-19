package com.aca.acacourierservice.entity;

import jakarta.persistence.*;

import java.util.Objects;


@Entity
@Table(name = "pickup_point")
public class PickupPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String zipCode;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String phoneNumber;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PickupPoint that = (PickupPoint) o;
        return id == that.id
                && Objects.equals(city, that.city)
                && Objects.equals(country, that.country)
                && Objects.equals(zipCode, that.zipCode)
                && Objects.equals(address, that.address)
                && Objects.equals(phoneNumber, that.phoneNumber)
                && Objects.equals(store, that.store);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id,
                city,
                country,
                zipCode,
                address,
                phoneNumber,
                store);
    }
}