package com.aca.acacourierservice.model;

import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.validation.OnCreate;
import com.aca.acacourierservice.validation.OnUpdate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public class StoreJson {
    private long id;
    @NotEmpty(message = "The name field must not be empty")
    @Size(min = 2, max = 20, message = "Store name length should be 2-20")
    private String name;
    @Valid
    @Null(groups = OnUpdate.class, message = "You can't update store admin with store. For updating store admin call /admin/update endpoint")
    private User admin;
    @Valid
    @Null(groups = OnUpdate.class, message = "You can't update pickup points with store. For updating pickup points call /pickupPoint endpoint")
    private List<PickupPointJson> pickupPoints;
    @Pattern(regexp = "^(https:\\/\\/)?[^\\s/$.?#]+\\.[^\\s]*$", message = "The url ${validatedValue} is invalid")
    private String storeUrl;
    @Pattern(regexp = "^[+][0-9]{10,15}$", message = "Invalid phone number")
    private String phoneNumber;
    @Null(groups = OnCreate.class, message = "You don't need to enter api key because api key will be generated")
    @Null(groups = OnUpdate.class, message = "You can't update api key")
    private String apiKey;
    @Null(groups = OnCreate.class, message = "You don't need to enter api secret because api key will be generated")
    @Null(groups = OnUpdate.class, message = "You can't update api secret")
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