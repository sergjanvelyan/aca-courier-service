package com.aca.acacourierservice.model;

import com.aca.acacourierservice.entity.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public class StoreJson {
    private long id;
    @NotNull
    @Pattern(regexp = "^[A-Za-z]+(?:[ A-Za-z'-]*[A-Za-z]+)*$")
    private String name;
    @NotNull
    private User admin;
    private List<PickupPointJson> pickupPoints;
    @Pattern(regexp = "^(https?|ftp)?://(?!.*example)[^\\s/$.?#].[^\\s]*$")
    private String storeUrl;
    @Pattern(regexp = "^\\+?\\d+$")
    private String phoneNumber;
    private String apiKey;
    private String apiSecret;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public List<PickupPointJson> getPickupPoints() {
        return pickupPoints;
    }

    public void setPickupPoints(List<PickupPointJson> pickupPoints) {
        this.pickupPoints = pickupPoints;
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public void setStoreUrl(String storeUrl) {
        this.storeUrl = storeUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}