package com.aca.acacourierservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;
import java.util.Objects;

@Entity
@SQLDelete(sql = "UPDATE store SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NotEmpty(message = "The name field must not be empty")
    private String name;
    @OneToOne
    @NotNull(message = "Admin must not be null")
    @JoinColumn(name = "admin_id")
    private User admin;
    @OneToMany(mappedBy = "store")
    private List<PickupPoint> pickupPoints;
    @Column
    private String storeUrl;
    @Column
    private String phoneNumber;
    @Column(unique = true)
    private String apiKey;
    @Column(unique = true)
    private String apiSecret;
    private Boolean deleted = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<PickupPoint> getPickupPoints() {
        return pickupPoints;
    }

    public void setPickupPoints(List<PickupPoint> pickupPoints) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return Objects.equals(id, store.id)
                && Objects.equals(name, store.name)
                && Objects.equals(admin, store.admin)
                && Objects.equals(pickupPoints, store.pickupPoints)
                && Objects.equals(storeUrl, store.storeUrl)
                && Objects.equals(phoneNumber, store.phoneNumber)
                && Objects.equals(apiKey, store.apiKey)
                && Objects.equals(apiSecret, store.apiSecret)
                && Objects.equals(deleted, store.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
                name,
                admin,
                pickupPoints,
                storeUrl,
                phoneNumber,
                apiKey,
                apiSecret,
                deleted);
    }
}