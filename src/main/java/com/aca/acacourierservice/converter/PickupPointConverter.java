package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.model.PickupPointJson;
import com.aca.acacourierservice.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class PickupPointConverter implements Converter<PickupPoint, PickupPointJson> {

    private final StoreService storeService;

    @Autowired
    public PickupPointConverter(@Lazy StoreService storeService) {
        this.storeService = storeService;
    }

    @Override
    public PickupPoint convertToEntity(PickupPointJson model, PickupPoint entity) {
        entity.setStore(storeService.getStoreById(model.getStoreId()));
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
        return convertToEntity(model,entity);
    }

   @Override
    public PickupPointJson convertToModel(PickupPoint entity) {
        PickupPointJson model = new PickupPointJson();
        model.setStoreId(entity.getStore().getId());
        model.setCity(entity.getCity());
        model.setCountry(entity.getCountry());
        model.setAddress(entity.getAddress());
        model.setPhoneNumber(entity.getPhoneNumber());
        model.setZipCode(entity.getZipCode());

        return model;
    }
}