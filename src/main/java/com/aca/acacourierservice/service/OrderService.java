package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.OrderConverter;
import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.*;
import com.aca.acacourierservice.repository.OrderRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class OrderService{
    private final OrderRepository orderRepository;
    private final OrderConverter orderConverter;
    private final StatusUpdateTimeService statusUpdateTimeService;
    private final UserService userService;
    private final StoreService storeService;

    static final double DELIVERY_PRICE_USD_KG = 2;
    static final double STANDARD_FEE_USD = 1;
    static final double FEE_COEFFICIENT_OF_SMALL_SIZE = 0.9;

    static final double FEE_COEFFICIENT_OF_MEDIUM_SIZE = 1;
    static final double FEE_COEFFICIENT_OF_LARGE_SIZE = 1.1;
    static final double FEE_COEFFICIENT_OF_EXTRA_LARGE_SIZE = 1.2;
    @Autowired
    public OrderService(OrderRepository orderRepository, OrderConverter orderConverter, @Lazy StatusUpdateTimeService statusUpdateTimeService, UserService userService, StoreService storeService) {
        this.orderRepository = orderRepository;
        this.orderConverter = orderConverter;
        this.statusUpdateTimeService = statusUpdateTimeService;
        this.userService = userService;
        this.storeService = storeService;
    }
    @Transactional
    public String addOrder(@Valid OrderJson orderJson){
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
    public void updateOrderStatus(@Min(1) long id,@Valid StatusInfoJson statusInfoJson) throws CourierServiceException {
        Order order = getOrderById(id);
        if(order.getCourier()==null){
            throw new CourierServiceException("Can't update unassigned order status");
        }
        Order.Status previousStatus = order.getStatus();
        Order.Status newStatus = statusInfoJson.getStatus();
        int comparisonResult = previousStatus.compareTo(newStatus);
        if (comparisonResult == 0) {
            throw new CourierServiceException("Can't update to same status");
        } else if (comparisonResult > 0) {
            throw new CourierServiceException("Can't update status "+previousStatus+" to status "+newStatus);
        }
        StatusUpdateTimeJson statusUpdateTimeJson = new StatusUpdateTimeJson();
        statusUpdateTimeJson.setOrderId(id);
        statusUpdateTimeJson.setUpdatedFrom(order.getStatus());
        statusUpdateTimeJson.setUpdatedTo(statusInfoJson.getStatus());
        statusUpdateTimeJson.setUpdateTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        statusUpdateTimeJson.setAdditionalInfo(statusInfoJson.getAdditionalInfo());
        statusUpdateTimeService.addStatusUpdateTime(statusUpdateTimeJson);
        order.setStatus(statusInfoJson.getStatus());
        if(statusInfoJson.getStatus() == Order.Status.DELIVERED){
            order.setOrderDeliveredTime(statusUpdateTimeJson.getUpdateTime());
        }
        orderRepository.save(order);
    }
    @Transactional
    public void assignCourierToOrder(@Min(1) long orderId,@Min(1) long courierId) throws CourierServiceException {
        Order order;
        try{
            order = getOrderById(orderId);
        }catch (CourierServiceException e){
            throw new CourierServiceException("Can't assign nonexistent order to courier");
        }
        if(order.getCourier()!=null){
            throw new CourierServiceException("Order already assigned,try to assign another order");
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
    public Order getOrderById(@Min(1) long id) throws CourierServiceException {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()){
            throw new CourierServiceException("Order not found");
        }
        return orderOptional.get();
    }
    public Specification<Order> getOrderFilterSpecification(FilteringInfo filteringInfo){
        return (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();
            if (filteringInfo.getCity() != null) {
                predicates.add(criteriaBuilder.equal(root.get("city"),filteringInfo.getCity()));
            }
            if (filteringInfo.getCountry() != null) {
                predicates.add(criteriaBuilder.equal(root.get("country"),filteringInfo.getCountry()));
            }
            if (filteringInfo.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"),filteringInfo.getStatus()));
            }
            if (filteringInfo.getZipCode() != null) {
                predicates.add(criteriaBuilder.equal(root.get("zipCode"),filteringInfo.getZipCode()));
            }
            if (filteringInfo.getSize() != null) {
                predicates.add(criteriaBuilder.equal(root.get("size"),filteringInfo.getSize()));
            }
            if (filteringInfo.getDeliveryPriceMin() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("deliveryPrice"), filteringInfo.getDeliveryPriceMin()));
            }
            if (filteringInfo.getDeliveryPriceMax() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("deliveryPrice"),filteringInfo.getDeliveryPriceMax()));
            }
            if (filteringInfo.getTotalPriceMin() != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("totalPrice"), filteringInfo.getTotalPriceMin()));
            }
            if (filteringInfo.getTotalPriceMax() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("totalPrice"), filteringInfo.getTotalPriceMax()));
            }
            if (filteringInfo.getWeightMin() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("weightKg"), filteringInfo.getWeightMin()));
            }
            if (filteringInfo.getWeightMax() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("weightKg"),filteringInfo.getWeightMax()));
            }
            if (filteringInfo.getOrderConfirmedTimeMin() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("orderConfirmedTime"), filteringInfo.getOrderConfirmedTimeMin()));
            }
            if (filteringInfo.getOrderConfirmedTimeMax() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("orderConfirmedTime"),filteringInfo.getOrderConfirmedTimeMax()));
            }
            if (filteringInfo.getOrderDeliveredTimeMin() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("orderDeliveredTime"), filteringInfo.getOrderDeliveredTimeMin()));
            }
            if (filteringInfo.getOrderDeliveredTimeMax() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("orderDeliveredTime"), filteringInfo.getOrderDeliveredTimeMax()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    public Page<Order> getFilteredOrders(Specification<Order> orderSpecification, @Min(0) int page, @Min(1) int size){
        return orderRepository.findAll(orderSpecification,PageRequest.of(page, size));
    }
    public Page<Order> getOrders(@Min(0) int page,@Min(1) int size){
        return orderRepository.findAll(PageRequest.of(page, size));
    }
    public Page<Order> getOrdersByCourierId(@Min(1) long courierId,@Min(0) int page,@Min(1) int size){
        return orderRepository.findAllByCourierId(courierId,PageRequest.of(page, size));
    }
    public Page<Order> getUnassignedOrders(@Min(0) int page,@Min(1) int size){
        return orderRepository.findAllByCourierIsNull(PageRequest.of(page,size));
    }
    public Page<Order> getOrdersByStoreAdminId(@Min(1) long storeAdminId, @Min(0) int page, @Min(1) int size){
        return orderRepository.findAllByStore_Admin_Id(storeAdminId,PageRequest.of(page, size));
    }
    public double calculateDeliveryPrice(@Valid ItemOrderInfo itemOrderInfo,@Min(1) long storeId){
        Order.Size size = itemOrderInfo.getSize();
        double weight = itemOrderInfo.getWeightKg();
        String destinationCountry = itemOrderInfo.getCountry();
        Store store = storeService.getStoreById(storeId);
        List<PickupPoint> pickupPoints = store.getPickupPoints();
        double additionalFeeUSD = 0;
        if(!isSameCountry(pickupPoints,destinationCountry)){
            additionalFeeUSD+=2;
        }
        return STANDARD_FEE_USD+
                additionalFeeUSD+
                switch (size){
            case SMALL -> weight*DELIVERY_PRICE_USD_KG*FEE_COEFFICIENT_OF_SMALL_SIZE;
            case MEDIUM -> weight*DELIVERY_PRICE_USD_KG*FEE_COEFFICIENT_OF_MEDIUM_SIZE;
            case LARGE -> weight*DELIVERY_PRICE_USD_KG*FEE_COEFFICIENT_OF_LARGE_SIZE;
            case EXTRA_LARGE -> weight*DELIVERY_PRICE_USD_KG*FEE_COEFFICIENT_OF_EXTRA_LARGE_SIZE;
        };
    }
    public boolean isSameCountry(List<PickupPoint> pickupPoints,String destinationCountry){
        for (PickupPoint pickupPoint:pickupPoints) {
            if (pickupPoint.getCountry().equalsIgnoreCase(destinationCountry)){
                return true;
            }
        }
        return false;
    }
}