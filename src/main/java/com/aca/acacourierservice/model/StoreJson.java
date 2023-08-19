package com.aca.acacourierservice.model;

import com.aca.acacourierservice.entity.PickupPoint;

import java.util.List;

public class StoreJson {
    private String name;

    private long adminId;

    //Change generic type to PickupPointJson after writing it
    private List<PickupPoint> pickupPoints;

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

    public List<PickupPoint> getPickupPoints() {
        return pickupPoints;
    }

    public void setPickupPoints(List<PickupPoint> pickupPoints) {
        this.pickupPoints = pickupPoints;
    }
}
