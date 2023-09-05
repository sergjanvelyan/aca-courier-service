package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.model.OrderJson;
import com.aca.acacourierservice.model.OrderListJson;
import com.aca.acacourierservice.service.StoreService;
import com.aca.acacourierservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderConverter implements Converter<Order, OrderJson> {
    //TODO: remove field injections
    private final StoreService storeService;
    private final UserService userService;

    @Autowired
    public OrderConverter(@Lazy StoreService storeService, @Lazy UserService userService) {
        this.storeService = storeService;
        this.userService = userService;
    }

    @Override
    public Order convertToEntity(OrderJson model, Order entity) {
        entity.setOrderId(model.getOrderId());
        entity.setTrackingNumber(model.getTrackingNumber());
        //TODO: remove next line
        entity.setStore(storeService.getStoreById(model.getStoreId()));
        entity.setFullName(model.getFullName());
        entity.setCountry(model.getCountry());
        entity.setCity(model.getCity());
        entity.setAddress(model.getAddress());
        entity.setPhone(model.getPhone());
        entity.setZipCode(model.getZipCode());
        entity.setWeightKg(model.getWeightKg());
        entity.setSize(model.getSize());
        //TODO: remove next line
        entity.setCourier(userService.getUserById(model.getCourierId()));
        entity.setDeliveryPrice(model.getDeliveryPrice());
        entity.setTotalPrice(model.getTotalPrice());
        entity.setStatus(model.getStatus());
        entity.setOrderConfirmedTime(model.getOrderConfirmedTime());
        entity.setOrderDeliveredTime(model.getOrderDeliveredTime());
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
        model.setTrackingNumber(entity.getTrackingNumber());
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
        model.setOrderConfirmedTime(entity.getOrderConfirmedTime());
        model.setOrderDeliveredTime(entity.getOrderDeliveredTime());
        return model;
    }
    public OrderListJson convertToListModel(Page<Order> orders){
        OrderListJson orderListJson = new OrderListJson();
        orderListJson.setTotalCount(orders.getTotalElements());
        List<OrderJson> ordersJson = new ArrayList<>();
        for (Order order:orders){
            ordersJson.add(convertToModel(order));
        }
        orderListJson.setOrderListJson(ordersJson);
        return orderListJson;
    }
}