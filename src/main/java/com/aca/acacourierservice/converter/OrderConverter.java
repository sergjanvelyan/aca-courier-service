package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.model.OrderJson;
import com.aca.acacourierservice.model.OrderListJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderConverter implements Converter<Order, OrderJson> {
    private final StatusUpdateTimeConverter statusUpdateTimeConverter;
    @Autowired
    public OrderConverter(StatusUpdateTimeConverter statusUpdateTimeConverter) {
        this.statusUpdateTimeConverter = statusUpdateTimeConverter;
    }
    @Override
    public Order convertToEntity(OrderJson model, Order entity) {
        entity.setOrderId(model.getOrderId());
        entity.setTrackingNumber(model.getTrackingNumber());
        entity.setFullName(model.getFullName());
        entity.setCountry(model.getCountry());
        entity.setCity(model.getCity());
        entity.setAddress(model.getAddress());
        entity.setPhone(model.getPhone());
        entity.setZipCode(model.getZipCode());
        entity.setWeightKg(model.getWeightKg());
        entity.setSize(model.getSize());
        entity.setDeliveryPrice(model.getDeliveryPrice());
        entity.setTotalPrice(model.getTotalPrice());
        if(model.getStatus()!=null){
            entity.setStatus(model.getStatus());
        }
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
        model.setId(entity.getId());
        model.setOrderId(entity.getOrderId());
        model.setTrackingNumber(entity.getTrackingNumber());
        if(entity.getStore()!=null){
            model.setStoreId(entity.getStore().getId());
        }
        model.setFullName(entity.getFullName());
        model.setCountry(entity.getCountry());
        model.setCity(entity.getCity());
        model.setAddress(entity.getAddress());
        model.setPhone(entity.getPhone());
        model.setZipCode(entity.getZipCode());
        model.setWeightKg(entity.getWeightKg());
        model.setSize(entity.getSize().toString());
        if(entity.getCourier()!=null){
            model.setCourierId(entity.getCourier().getId());
        }
        model.setDeliveryPrice(entity.getDeliveryPrice());
        model.setTotalPrice(entity.getTotalPrice());
        model.setStatus(entity.getStatus().toString());
        model.setOrderConfirmedTime(entity.getOrderConfirmedTime());
        model.setOrderDeliveredTime(entity.getOrderDeliveredTime());
        model.setStatusUpdateHistory(statusUpdateTimeConverter.convertToListModel(entity.getStatusUpdateTimeList()));
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