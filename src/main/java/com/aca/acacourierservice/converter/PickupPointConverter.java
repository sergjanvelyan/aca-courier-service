package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.model.PickupPointJson;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PickupPointConverter implements Converter<PickupPoint, PickupPointJson> {
    @Override
    public PickupPoint convertToEntity(PickupPointJson model, PickupPoint entity) {
        entity.setCity(model.getCity());
        entity.setCountry(model.getCountry());
        entity.setAddress(model.getAddress());
        entity.setPhoneNumber(model.getPhoneNumber());
        entity.setZipCode(model.getZipCode());
        return entity;
    }
    @Override
    public PickupPoint convertToEntity(PickupPointJson model) {
        PickupPoint entity = new PickupPoint();
        return convertToEntity(model, entity);
    }
    @Override
    public PickupPointJson convertToModel(PickupPoint entity) {
        PickupPointJson model = new PickupPointJson();
        if(entity.getStore()!=null){
            model.setStoreId(entity.getStore().getId());
        }
        model.setCity(entity.getCity());
        model.setCountry(entity.getCountry());
        model.setAddress(entity.getAddress());
        model.setPhoneNumber(entity.getPhoneNumber());
        model.setZipCode(entity.getZipCode());
        return model;
    }
    public List<PickupPointJson> convertToModelList(List<PickupPoint> pickupPoints){
        List<PickupPointJson> pickupPointJson = new ArrayList<>();
        for (PickupPoint pickupPoint:pickupPoints){
            pickupPointJson.add(convertToModel(pickupPoint));
        }
        return pickupPointJson;
    }
    public List<PickupPoint> convertToEntityList(List<PickupPointJson> pickupPointJsons){
        List<PickupPoint> pickupPoints = new ArrayList<>();
        for (PickupPointJson pickupPointJson:pickupPointJsons){
            pickupPoints.add(convertToEntity(pickupPointJson));
        }
        return pickupPoints;
    }
}