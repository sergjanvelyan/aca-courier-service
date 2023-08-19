package com.aca.acacourierservice.converter;

import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter{
    @Override
    public Object convertToEntity(Object model, Object entity) {
        return null;
    }

    @Override
    public Object convertToModel(Object entity, Object model) {
        return null;
    }
}
