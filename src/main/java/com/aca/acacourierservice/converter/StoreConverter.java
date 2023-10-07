package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.model.StoreJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StoreConverter implements Converter<Store, StoreJson> {
    private final PickupPointConverter pickupPointConverter;
    @Autowired
    public StoreConverter(PickupPointConverter pickupPointConverter) {
        this.pickupPointConverter = pickupPointConverter;
    }

    @Override
    public Store convertToEntity(StoreJson model, Store entity) {
        entity.setName(model.getName());
        entity.setPhoneNumber(model.getPhoneNumber());
        if (model.getPickupPoints() != null) {
            entity.setPickupPoints(pickupPointConverter.convertToEntityList(model.getPickupPoints()));
        }
        entity.setStoreUrl(model.getStoreUrl());
        entity.setApiKey(model.getApiKey());
        entity.setApiSecret(model.getApiSecret());
        entity.setAdmin(model.getAdmin());
        return entity;
    }

    @Override
    public Store convertToEntity(StoreJson model) {
        Store entity = new Store();
        entity.setName(model.getName());
        entity.setPhoneNumber(model.getPhoneNumber());
        if (model.getPickupPoints() != null) {
            entity.setPickupPoints(pickupPointConverter.convertToEntityList(model.getPickupPoints()));
        }
        entity.setStoreUrl(model.getStoreUrl());
        entity.setApiKey(model.getApiKey());
        entity.setApiSecret(model.getApiSecret());
        entity.setAdmin(model.getAdmin());
        return entity;
    }

    @Override
    public StoreJson convertToModel(Store entity) {
        StoreJson model = new StoreJson();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setAdmin(entity.getAdmin());
        model.setPhoneNumber(entity.getPhoneNumber());
        model.setStoreUrl(entity.getStoreUrl());
        model.setApiKey(entity.getApiKey());
        model.setApiSecret(entity.getApiSecret());
        model.setPickupPoints(pickupPointConverter.convertToModelList(entity.getPickupPoints()));
        return model;
    }
}