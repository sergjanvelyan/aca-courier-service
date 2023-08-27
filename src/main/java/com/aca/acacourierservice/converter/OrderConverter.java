package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.model.OrderJson;
import com.aca.acacourierservice.service.StoreService;
import com.aca.acacourierservice.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter implements Converter<Order, OrderJson> {
    private final StoreService storeService;
    private final UserService userService;

    public OrderConverter(StoreService storeService, UserService userService) {
        this.storeService = storeService;
        this.userService = userService;
    }

    @Override
    public Order convertToEntity(OrderJson model, Order entity) {
        entity.setOrderId(model.getOrderId());
        entity.setTrackingId(model.getTrackingId());
        entity.setStore(storeService.getStoreById(model.getStoreId()));
        entity.setFullName(model.getFullName());
        entity.setCountry(model.getCountry());
        entity.setCity(model.getCity());
        entity.setAddress(model.getAddress());
        entity.setPhone(model.getPhone());
        entity.setZipCode(model.getZipCode());
        entity.setWeightKg(model.getWeightKg());
        entity.setSize(model.getSize());
        entity.setCourier(userService.getUserById(model.getCourierId()));
        entity.setDeliveryPrice(model.getDeliveryPrice());
        entity.setTotalPrice(model.getTotalPrice());
        entity.setStatus(model.getStatus());
        return entity;
    }
    @Override
    public Order convertToEntity(OrderJson model) {
        Order entity = new Order();
        return convertToEntity(model,entity);
    }
    @Override
    public OrderJson convertToModel(Order entity) {
        OrderJson model = new OrderJson();
        model.setOrderId(entity.getOrderId());
        model.setTrackingId(entity.getTrackingId());
        model.setStoreId(entity.getStore().getId());
        model.setFullName(entity.getFullName());
        model.setCountry(entity.getCountry());
        model.setCity(entity.getCity());
        model.setAddress(entity.getAddress());
        model.setPhone(entity.getPhone());
        model.setZipCode(entity.getZipCode());
        model.setWeightKg(entity.getWeightKg());
        model.setSize(entity.getSize());
        model.setCourierId(entity.getCourier().getId());
        model.setDeliveryPrice(entity.getDeliveryPrice());
        model.setTotalPrice(entity.getTotalPrice());
        model.setStatus(entity.getStatus());
        return model;
    }
}