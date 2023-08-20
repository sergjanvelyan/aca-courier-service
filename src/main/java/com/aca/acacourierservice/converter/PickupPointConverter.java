package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.model.PickupPointJson;
import com.aca.acacourierservice.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PickupPointConverter implements Converter<PickupPoint, PickupPointJson>{

    private final StoreService storeService;
    @Autowired
    public PickupPointConverter(StoreService storeService){this.storeService=storeService;}

    @Override
    public PickupPoint convertToEntity(PickupPointJson model, PickupPoint entity) {
        entity.setStore(storeService.getStoreById(model.getStoreId()));
        entity.setCity(model.getCity());
        entity.setStreet(model.getStreet());
        entity.setBuilding(model.getBuilding());

        return entity;
    }

    @Override
    public PickupPoint convertToEntity(PickupPointJson model) {
        PickupPoint entity = new PickupPoint();
        entity.setStore(storeService.getStoreById(model.getStoreId()));
        entity.setCity(model.getCity());
        entity.setStreet(model.getStreet());
        entity.setBuilding(model.getBuilding());

        return entity;
    }

    @Override
    public PickupPointJson convertToModel(PickupPoint entity) {
        PickupPointJson model= new PickupPointJson();
        model.setStoreId(entity.getStore().getId());
        model.setCity(entity.getCity());
        model.setStreet(entity.getStreet());;
        model.setBuilding(entity.getBuilding());

        return model;
    }
}
