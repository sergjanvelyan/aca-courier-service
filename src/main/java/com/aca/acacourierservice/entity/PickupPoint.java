package com.aca.acacourierservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pickup_point")
public class PickupPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String street;
    @Column(nullable = false)
    private String building;
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String name) {
        this.city = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String address) {
        this.street = address;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
