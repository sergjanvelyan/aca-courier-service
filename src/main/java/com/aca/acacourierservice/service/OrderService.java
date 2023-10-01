package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.OrderConverter;
import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.OrderJson;
import com.aca.acacourierservice.model.StatusUpdateTimeJson;
import com.aca.acacourierservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class OrderService{
    private final OrderRepository orderRepository;
    private final OrderConverter orderConverter;
    private final StatusUpdateTimeService statusUpdateTimeService;
    private final UserService userService;
    private final StoreService storeService;
    @Autowired
    public OrderService(OrderRepository orderRepository, OrderConverter orderConverter, @Lazy StatusUpdateTimeService statusUpdateTimeService, UserService userService, StoreService storeService) {
        this.orderRepository = orderRepository;
        this.orderConverter = orderConverter;
        this.statusUpdateTimeService = statusUpdateTimeService;
        this.userService = userService;
        this.storeService = storeService;
    }
    @Transactional
     public String addOrder(OrderJson orderJson){
        Order order = orderConverter.convertToEntity(orderJson);
        order.setStore(storeService.getStoreById(orderJson.getStoreId()));
        order.setStatus(Order.Status.NEW);
        order.setOrderConfirmedTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        long id = orderRepository.save(order).getId();
        StatusUpdateTimeJson statusUpdateTimeJson = new StatusUpdateTimeJson();
        statusUpdateTimeJson.setOrderId(id);
        statusUpdateTimeJson.setUpdatedTo(Order.Status.NEW);
        statusUpdateTimeJson.setUpdateTime(order.getOrderConfirmedTime());
        statusUpdateTimeJson.setAdditionalInfo("Created new order");
        statusUpdateTimeService.addStatusUpdateTime(statusUpdateTimeJson);
        return order.getTrackingNumber();
     }
    @Transactional
    public void updateOrderStatus(long id,Order.Status status,String additionalInfo) throws CourierServiceException {
        Order order = getOrderById(id);
        if(order.getCourier()==null){
            throw new CourierServiceException("Can't update unassigned order status");
        }
        if(order.getStatus()==status){
            throw new CourierServiceException("Can't update to same status");
        }
        StatusUpdateTimeJson statusUpdateTimeJson = new StatusUpdateTimeJson();
        statusUpdateTimeJson.setOrderId(id);
        statusUpdateTimeJson.setUpdatedFrom(order.getStatus());
        statusUpdateTimeJson.setUpdatedTo(status);
        statusUpdateTimeJson.setUpdateTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        statusUpdateTimeJson.setAdditionalInfo(additionalInfo);
        statusUpdateTimeService.addStatusUpdateTime(statusUpdateTimeJson);
        order.setStatus(status);
        if(status== Order.Status.DELIVERED){
            order.setOrderDeliveredTime(statusUpdateTimeJson.getUpdateTime());
        }
        orderRepository.save(order);
    }
    @Transactional
    public void assignCourierToOrder(long orderId,long courierId) throws CourierServiceException {
        Order order;
        try{
            order = getOrderById(orderId);
        }catch (CourierServiceException e){
            throw new CourierServiceException("Can't assign nonexistent order to courier");
        }
        User courier;
        try {
            courier = userService.getUserById(courierId);
        }catch (CourierServiceException e){
            throw new CourierServiceException("Can't assign order to nonexistent courier");
        }
        if(courier.getRole()!= User.Role.ROLE_COURIER){
            throw new CourierServiceException("Can't assign order(id="+orderId+") to a user other than courier");
        }
            order.setCourier(courier);
        orderRepository.save(order);
    }
    public Order getOrderById(long id) throws CourierServiceException {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()){
            throw new CourierServiceException("Order not found");
        }
        return orderOptional.get();
    }
    public Page<Order> getOrders(int page,int size){
        return orderRepository.findAll(PageRequest.of(page, size));
    }
    public Page<Order> getOrdersByCourierId(long courierId,int page,int size){
        return orderRepository.findAllByCourierId(courierId,PageRequest.of(page, size));
    }
    public Page<Order> getUnassignedOrders(int page,int size){
        return orderRepository.findAllByCourierIsNull(PageRequest.of(page,size));
    }
    public Page<Order> getOrdersByStoreId(long storeId,int page,int size){
        return orderRepository.findAllByStoreId(storeId,PageRequest.of(page, size));
    }
}