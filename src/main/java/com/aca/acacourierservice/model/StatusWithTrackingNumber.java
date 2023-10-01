package com.aca.acacourierservice.model;

public class StatusWithTrackingNumber extends Status {
    private String trackingNumber;

    public StatusWithTrackingNumber(String status, String trackingNumber) {
        super(status);
        this.trackingNumber = trackingNumber;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }
}
