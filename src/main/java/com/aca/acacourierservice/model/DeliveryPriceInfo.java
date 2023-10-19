package com.aca.acacourierservice.model;

import jakarta.validation.constraints.Positive;

public class DeliveryPriceInfo {
    @Positive
    private Double deliveryPrice;
    String currency;

    public DeliveryPriceInfo(Double deliveryPrice, String currency) {
        this.deliveryPrice = deliveryPrice;
        this.currency = currency;
    }

    public Double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(Double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
