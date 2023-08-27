package com.aca.acacourierservice.model;

import java.util.List;

public class StoreJson {
    private String name;
    private long adminId;
    private List<PickupPointJson> pickupPoints;
    private String storeUrl;
    private String phoneNumber;
    private String apiKey;
    private String apiSecret;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAdminId() {
        return adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
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
}