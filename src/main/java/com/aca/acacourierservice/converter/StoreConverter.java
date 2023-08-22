package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.model.StoreJson;
import org.springframework.stereotype.Component;

@Component
public class StoreConverter implements Converter<Store, StoreJson> {
    @Override
    public Store convertToEntity(StoreJson model, Store entity) {
        return null;
    }
    @Override
    public Store convertToEntity(StoreJson model) {
        return null;
    }
    @Override
    public StoreJson convertToModel(Store entity) {
        return null;
    }
}