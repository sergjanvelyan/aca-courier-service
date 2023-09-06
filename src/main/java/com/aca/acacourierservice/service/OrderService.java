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
    @Autowired
    public OrderService(OrderRepository orderRepository, OrderConverter orderConverter, StatusUpdateTimeService statusUpdateTimeService, UserService userService) {
        this.orderRepository = orderRepository;
        this.orderConverter = orderConverter;
        this.statusUpdateTimeService = statusUpdateTimeService;
        this.userService = userService;
    }
    @Transactional
     public long addOrder(OrderJson orderJson){
        //TODO: Here we can do some additional work before adding order in database
        Order order = orderConverter.convertToEntity(orderJson);
        order.setOrderConfirmedTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        return orderRepository.save(order).getId();
     }
    @Transactional
    public void updateOrderStatus(long id,Order.Status status,String additionalInfo) throws CourierServiceException {
        try{
            Order order = getOrderById(id);
            StatusUpdateTimeJson statusUpdateTimeJson = new StatusUpdateTimeJson();
            statusUpdateTimeJson.setOrderId(id);
            statusUpdateTimeJson.setUpdatedFrom(order.getStatus());
            statusUpdateTimeJson.setUpdatedTo(status);
            statusUpdateTimeJson.setUpdateTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
            statusUpdateTimeJson.setAdditionalInfo(additionalInfo);
            statusUpdateTimeService.addStatusUpdateTime(statusUpdateTimeJson);
            order.setStatus(status);
            orderRepository.save(order);
        }catch (CourierServiceException e){
            throw new CourierServiceException("Can't update nonexistent order(id="+id+")");
        }
    }
    @Transactional
    public void assignCourierToOrder(long orderId,long courierId) throws CourierServiceException {
        Order order;
        try{
            order = getOrderById(orderId);
        }catch (CourierServiceException e){
            throw new CourierServiceException("Can't assign nonexistent order(id="+orderId+")+ to courier(id="+courierId+")");
        }
        User courier;
        try {
            courier = userService.getUserById(courierId);
        }catch (CourierServiceException e){
            throw new CourierServiceException("Can't assign order(id="+orderId+")+ to nonexistent courier(id="+courierId+")");
        }
        order.setCourier(courier);
        orderRepository.save(order);
    }
    public Order getOrderById(long id) throws CourierServiceException {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()){
            throw new CourierServiceException("There is no order with id("+id+")");
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