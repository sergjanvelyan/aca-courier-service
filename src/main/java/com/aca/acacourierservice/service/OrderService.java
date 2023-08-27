package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.OrderConverter;
import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.OrderJson;
import com.aca.acacourierservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService{
    private final OrderRepository orderRepository;
    private final OrderConverter orderConverter;
    @Autowired
    public OrderService(OrderRepository orderRepository, OrderConverter orderConverter) {
        this.orderRepository = orderRepository;
        this.orderConverter = orderConverter;
    }
    @Transactional
     public Order addOrder(OrderJson orderJson){
        //TODO: Here we can do some additional work before adding order in database
        Order order = orderConverter.convertToEntity(orderJson);
        return orderRepository.save(order);
     }
    @Transactional
    public OrderJson updateOrder(long id,OrderJson orderJson){
        Order order = getOrderById(id);
        order = orderConverter.convertToEntity(orderJson,order);
        return orderConverter.convertToModel(orderRepository.save(order));
    }
    @Transactional
    public OrderJson updateOrderStatus(long id,Order.Status status){
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderConverter.convertToModel(orderRepository.save(order));
    }
    public Order getOrderById(long id){
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()){
            throw new CourierServiceException("There is no order with id("+id+")");
        }
        return orderOptional.get();
    }
    public Order getOrderByTrackingId(String trackingId){
        Optional<Order> orderOptional = orderRepository.findByTrackingId(trackingId);
        if (orderOptional.isEmpty()){
            throw new CourierServiceException("There is no order with trackingId("+trackingId+")");
        }
        return orderOptional.get();
    }
    public Page<Order> getOrdersByStoreId(long storeId,int page,int size){
        return orderRepository.findAllByStoreId(storeId,PageRequest.of(page, size));
    }
    public Page<Order> getOrdersByCustomerName(String fullName,int page,int size){
        return orderRepository.findAllByFullName(fullName,PageRequest.of(page, size));
    }
    public Page<Order> getOrdersByStatus(Order.Status status,int page,int size){
        return orderRepository.findAllByStatus(status,PageRequest.of(page, size));
    }
    public Page<Order> getOrdersByStoreIdAndStatus(long storeId,Order.Status status,int page,int size){
        return orderRepository.findAllByStoreIdAndStatus(storeId,status,PageRequest.of(page, size));
    }
    @Transactional
    public void deleteOrder(long id){
        if(!orderRepository.existsById(id)){
            throw new CourierServiceException("There is no order with id("+id+")");
        }
        orderRepository.deleteById(id);
    }
}
