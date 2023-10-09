package com.aca.acacourierservice.model;

import com.aca.acacourierservice.validation.OnCreate;
import com.aca.acacourierservice.validation.OnUpdate;
import com.aca.acacourierservice.view.Lists;
import com.aca.acacourierservice.view.PrivateSecondLevel;
import com.aca.acacourierservice.view.Public;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class PickupPointJson {
    @JsonView(Lists.class)
    private long id;
    @JsonView(Public.class)
    @Pattern(regexp ="^(([a-zA-Z]{2,15})\\s?)+$",message = "Not valid city")
    private String city;
    @JsonView(Public.class)
    @Pattern(regexp ="^(([a-zA-Z]{2,15})\\s?)+$",message = "Not valid country")
    private String country;
    @JsonView(PrivateSecondLevel.class)
    @Pattern(regexp = "^(([a-zA-Z]{2,15}\\s?)+|[1-9]+)\\s([1-9]+[a-zA-Z]?|([1-9]+/[1-9]+))$",message = "Not valid address")
    private String address;
    @JsonView(Public.class)
    @Pattern(regexp ="^[A-Z0-9]{2,15}$",message = "Not valid zip code")
    private String zipCode;
    @JsonView(PrivateSecondLevel.class)
    @Pattern(regexp ="^[+][0-9]{10,15}$",message = "Phone number should be like +{country code}{phone number}")
    private String phoneNumber;
    @JsonView(Public.class)

    @Null(groups = {OnCreate.class,OnUpdate.class},message = "You don't need to enter store id")
    @Positive
    private Long storeId;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}