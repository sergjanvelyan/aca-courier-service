package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.model.StoreJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StoreConverter implements Converter<Store, StoreJson> {
    private final PickupPointConverter pickupPointConverter;
    private final UserConverter userConverter;
    @Autowired
    public StoreConverter(PickupPointConverter pickupPointConverter, UserConverter userConverter) {
        this.pickupPointConverter = pickupPointConverter;
        this.userConverter = userConverter;
    }

    @Override
    public Store convertToEntity(StoreJson model, Store entity) {
        if(model.getName()!=null){
            entity.setName(model.getName());
        }
        if(model.getPhoneNumber()!=null){
            entity.setPhoneNumber(model.getPhoneNumber());
        }
        if(model.getStoreUrl()!=null){
            entity.setStoreUrl(model.getStoreUrl());
        }
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
        entity.setAdmin(userConverter.convertToEntity(model.getAdmin()));
        return entity;
    }

    @Override
    public StoreJson convertToModel(Store entity) {
        StoreJson model = new StoreJson();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setAdmin(userConverter.convertToModel(entity.getAdmin()));
        model.setPhoneNumber(entity.getPhoneNumber());
        model.setStoreUrl(entity.getStoreUrl());
        model.setApiKey(entity.getApiKey());
        model.setApiSecret(entity.getApiSecret());
        model.setPickupPoints(pickupPointConverter.convertToModelList(entity.getPickupPoints()));
        return model;
    }
}