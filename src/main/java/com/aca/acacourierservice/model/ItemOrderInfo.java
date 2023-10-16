package com.aca.acacourierservice.model;

import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.validation.ValidEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class ItemOrderInfo {
    @NotNull(message = "Please enter weight in kgs")
    @Positive
    private Double weightKg;
    @NotNull(message = "Please enter size")
    @ValidEnum(enumClass = Order.Size.class)
    private String size;
    @NotNull(message = "Please enter destination country")
    @Pattern(regexp ="^(([a-zA-Z]{2,15})\\s?)+$",message = "Not valid country")
    private String country;

    public double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }

    public Order.Size getSize() {
        if(size==null){
            return null;
        }
        return Order.Size.valueOf(size.toUpperCase());
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
