package com.aca.acacourierservice.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @OneToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;
    @OneToMany(mappedBy = "store")
    private List<PickupPoint> pickupPoints;

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
}
