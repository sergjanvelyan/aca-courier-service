package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.OrderConverter;
import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.*;
import com.aca.acacourierservice.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final EntityManager entityManager;
    private final OrderRepository orderRepository;
    private final OrderConverter orderConverter;
    private final StatusUpdateTimeService statusUpdateTimeService;
    private final UserService userService;
    private final StoreService storeService;
    @Autowired
    public OrderService(EntityManager entityManager, OrderRepository orderRepository, OrderConverter orderConverter, @Lazy StatusUpdateTimeService statusUpdateTimeService, UserService userService, StoreService storeService) {
        this.entityManager = entityManager;
        this.orderRepository = orderRepository;
        this.orderConverter = orderConverter;
        this.statusUpdateTimeService = statusUpdateTimeService;
        this.userService = userService;
        this.storeService = storeService;
    }

    public List<Order> listOrdersByFilters(@Valid FilteringInfo filteringInfo, @Min(0) int page, @Min(1) int count) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> orderCriteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = orderCriteriaQuery.from(Order.class);

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
            predicates.add(criteriaBuilder.equal(root.get("zip_code"),filteringInfo.getZipCode()));
        }
        if (filteringInfo.getSize() != null) {
            predicates.add(criteriaBuilder.equal(root.get("size"),filteringInfo.getSize()));
        }
        if (filteringInfo.getDeliveryPriceMin() != null && filteringInfo.getDeliveryPriceMax() != null) {
            predicates.add(criteriaBuilder.between(root.get("delivery_price"), filteringInfo.getDeliveryPriceMin(), filteringInfo.getDeliveryPriceMax()));
        }
        if (filteringInfo.getTotalPriceMin() != null && filteringInfo.getTotalPriceMax() != null) {
            predicates.add(criteriaBuilder.between(root.get("total_price"), filteringInfo.getTotalPriceMin(), filteringInfo.getTotalPriceMax()));
        }
        if (filteringInfo.getWeightMin() != null && filteringInfo.getWeightMax() != null) {
            predicates.add(criteriaBuilder.between(root.get("weight"), filteringInfo.getWeightMin(), filteringInfo.getWeightMax()));
        }
        if (filteringInfo.getOrderConfirmedTimeMin() != null && filteringInfo.getOrderConfirmedTimeMax() != null) {
            predicates.add(criteriaBuilder.between(root.get("order_confirmed_time"), filteringInfo.getOrderConfirmedTimeMin(), filteringInfo.getOrderConfirmedTimeMax()));
        }
        if (filteringInfo.getOrderDeliveredTimeMin() != null && filteringInfo.getOrderDeliveredTimeMax() != null) {
            predicates.add(criteriaBuilder.between(root.get("order_delivered_time"), filteringInfo.getOrderDeliveredTimeMin(), filteringInfo.getOrderDeliveredTimeMax()));
        }
        orderCriteriaQuery.select(root).where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        TypedQuery<Order> typedQuery = entityManager.createQuery(orderCriteriaQuery);
        typedQuery.setFirstResult((page-1) * count);
        typedQuery.setMaxResults(count);
        return typedQuery.getResultList();
    }
    public double calculateDeliveryPrice(@Valid ItemOrderInfo itemOrderInfo,@Min(1) long storeId){
        Order.Size size = itemOrderInfo.getSize();
        double weight = itemOrderInfo.getWeightKg();
        String destinationCountry = itemOrderInfo.getCountry();
        final double DELIVERY_PRICE_USD_KG = 2;
        Store store = storeService.getStoreById(storeId);
        List<PickupPoint> pickupPoints = store.getPickupPoints();
        double additionalFeeUSD = 1;
        if(!isSameCountry(pickupPoints,destinationCountry)){
            additionalFeeUSD+=2;
        }
        return additionalFeeUSD+switch (size){
            case SMALL -> weight*DELIVERY_PRICE_USD_KG*0.9;
            case MEDIUM -> weight*DELIVERY_PRICE_USD_KG;
            case LARGE -> weight*DELIVERY_PRICE_USD_KG*1.1;
            case EXTRA_LARGE -> weight*DELIVERY_PRICE_USD_KG*1.2;
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
        if(order.getStatus()==statusInfoJson.getStatus()){
            throw new CourierServiceException("Can't update to same status");
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
    public Page<Order> getOrders(@Min(0) int page,@Min(1) int size){
        return orderRepository.findAll(PageRequest.of(page, size));
    }
    public Page<Order> getOrdersByCourierId(@Min(1) long courierId,@Min(0) int page,@Min(1) int size){
        return orderRepository.findAllByCourierId(courierId,PageRequest.of(page, size));
    }
    public Page<Order> getUnassignedOrders(@Min(0) int page,@Min(1) int size){
        return orderRepository.findAllByCourierIsNull(PageRequest.of(page,size));
    }
    public Page<Order> getOrdersByStoreId(@Min(1) long storeId, @Min(0) int page, @Min(1) int size){
        return orderRepository.findAllByStoreId(storeId,PageRequest.of(page, size));
    }
}