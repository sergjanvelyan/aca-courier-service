package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.model.OrderJson;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter implements Converter<Order, OrderJson>{
    //we need field StoreService storeService to get store by id
    //we need field UserService userService,or maybe lather CourierService courierService to get courier by id
    @Override
    public Order convertToEntity(OrderJson model, Order entity) {
        //entity.setStore(here we should get store by storeId);
        entity.setTotalPrice(model.getTotalPrice());
        //entity.setCourier(here we should get courier by courierId);
        entity.setStatus(model.getStatus());
        return entity;
    }

    @Override
    public OrderJson convertToModel(Order entity, OrderJson model) {
        model.setStoreId(entity.getStore().getId());
        model.setTrackingId(entity.getTrackingId());
        model.setTotalPrice(entity.getTotalPrice());
        model.setCourierId(entity.getCourier().getId());
        model.setStatus(entity.getStatus());
        return model;
    }
}
