package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.model.StoreJson;
import org.springframework.stereotype.Component;

@Component
public class StoreConverter implements Converter<Store, StoreJson> {

    @Override
    public Store convertToEntity(StoreJson model, Store entity) {
        entity.setName(model.getName());
        entity.setPhoneNumber(model.getPhoneNumber());
        entity.setPickupPoints(model.getPickupPoints());
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
        entity.setPickupPoints(model.getPickupPoints());
        entity.setStoreUrl(model.getStoreUrl());
        entity.setApiKey(model.getApiKey());
        entity.setApiSecret(model.getApiSecret());
        entity.setAdmin(model.getAdmin());

        return entity;
    }

    @Override
    public StoreJson convertToModel(Store entity) {
        StoreJson model = new StoreJson();
        model.setName(entity.getName());
        model.setAdmin(entity.getAdmin());
        model.setPhoneNumber(entity.getPhoneNumber());
        model.setStoreUrl(entity.getStoreUrl());
        model.setApiKey(entity.getApiKey());
        model.setApiSecret(entity.getApiSecret());
        model.setPickupPoints(entity.getPickupPoints());

        return model;
    }
}