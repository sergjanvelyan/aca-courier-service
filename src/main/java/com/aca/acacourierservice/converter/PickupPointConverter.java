package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.model.PickupPointJson;
import org.springframework.stereotype.Component;

@Component
public class PickupPointConverter implements Converter<PickupPoint, PickupPointJson>{
    @Override
    public PickupPoint convertToEntity(PickupPointJson model, PickupPoint entity) {
        return null;
    }
    @Override
    public PickupPoint convertToEntity(PickupPointJson model) {
        return null;
    }
    @Override
    public PickupPointJson convertToModel(PickupPoint entity) {
        return null;
    }
}