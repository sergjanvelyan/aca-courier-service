package com.aca.acacourierservice.model;

import com.aca.acacourierservice.validation.OnCreate;
import com.aca.acacourierservice.validation.OnUpdate;
import com.aca.acacourierservice.view.Lists;
import com.aca.acacourierservice.view.PrivateFirstLevel;
import com.aca.acacourierservice.view.PrivateSecondLevel;
import com.aca.acacourierservice.view.Public;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public class StoreJson {
    @JsonView(Lists.class)
    private long id;
    @NotEmpty(message = "The name field must not be empty")
    @Size(min = 2, max = 30, message = "Store name length should be 2-30")
    @JsonView(Public.class)
    private String name;
    @Valid
    @Null(groups = OnUpdate.class, message = "You can't update store admin with store. For updating store admin call /admin/update endpoint")
    @JsonView(PrivateSecondLevel.class)
    private UserJson admin;
    @Valid
    @Null(groups = OnUpdate.class, message = "You can't update pickup points with store. For updating pickup points call /pickupPoint endpoint")
    @JsonView(PrivateSecondLevel.class)
    private List<PickupPointJson> pickupPoints;
    @Pattern(regexp = "^(https:\\/\\/)?[^\\s/$.?#]+\\.[^\\s]*$", message = "The url ${validatedValue} is invalid")
    @JsonView(Public.class)
    private String storeUrl;
    @Pattern(regexp = "^[+][0-9]{10,15}$", message = "Invalid phone number")
    @JsonView(Public.class)
    private String phoneNumber;
    @Null(groups = OnCreate.class, message = "You don't need to enter api key because api key will be generated")
    @Null(groups = OnUpdate.class, message = "You can't update api key")
    @JsonView(PrivateFirstLevel.class)
    private String apiKey;
    @Null(groups = OnCreate.class, message = "You don't need to enter api secret because api key will be generated")
    @Null(groups = OnUpdate.class, message = "You can't update api secret")
    @JsonView(PrivateFirstLevel.class)
    private String apiSecret;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserJson getAdmin() {
        return admin;
    }

    public void setAdmin(UserJson admin) {
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