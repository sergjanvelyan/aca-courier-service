package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.StatusUpdateTime;
import com.aca.acacourierservice.model.StatusUpdateTimeJson;
import org.springframework.stereotype.Component;

@Component
public class StatusUpdateTimeConverter implements Converter<StatusUpdateTime, StatusUpdateTimeJson>{
    @Override
    public StatusUpdateTime convertToEntity(StatusUpdateTimeJson model, StatusUpdateTime entity) {
        return null;
    }

    @Override
    public StatusUpdateTime convertToEntity(StatusUpdateTimeJson model) {
        return null;
    }

    @Override
    public StatusUpdateTimeJson convertToModel(StatusUpdateTime entity) {
        return null;
    }
}
