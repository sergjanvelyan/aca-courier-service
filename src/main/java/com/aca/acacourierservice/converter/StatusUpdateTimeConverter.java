package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.StatusUpdateTime;
import com.aca.acacourierservice.model.StatusUpdateTimeJson;
import com.aca.acacourierservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class StatusUpdateTimeConverter implements Converter<StatusUpdateTime, StatusUpdateTimeJson>{
    private final OrderService orderService;
    @Autowired
    public StatusUpdateTimeConverter(@Lazy OrderService orderService) {
        this.orderService = orderService;
    }
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
