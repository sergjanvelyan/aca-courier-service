package com.aca.acacourierservice.controller;

import com.aca.acacourierservice.aspect.ValidateApiKeySecret;
import com.aca.acacourierservice.converter.OrderConverter;
import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.exception.InvalidStoreCredentialsException;
import com.aca.acacourierservice.model.*;
import com.aca.acacourierservice.service.OrderService;
import com.aca.acacourierservice.service.UserService;
import com.aca.acacourierservice.validation.OnCreate;
import com.aca.acacourierservice.view.Lists;
import com.aca.acacourierservice.view.PrivateSecondLevel;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@Validated
@RequestMapping(value = "/order")
public class OrderRestController {
    private final OrderService orderService;
    private final OrderConverter orderConverter;
    private final UserService userService;
    @Autowired
    public OrderRestController(OrderService orderService, OrderConverter orderConverter, UserService userService) {
        this.orderService = orderService;
        this.orderConverter = orderConverter;
        this.userService = userService;
    }
    @ExceptionHandler
    public ResponseEntity<Status> exceptionHandler(InvalidStoreCredentialsException e){
        return new ResponseEntity<>(new Status(e.getMessage()),HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/create")
    @ValidateApiKeySecret
    @Validated(OnCreate.class)
    public StatusWithTrackingNumber createOrder(
            @RequestBody @Valid OrderJson orderJson,
            HttpServletRequest request){
        orderJson.setStoreId((Long) request.getAttribute("storeId"));
        String trackingNumber = orderService.addOrder(orderJson);
        return new StatusWithTrackingNumber("Created order",trackingNumber);
    }

    @GetMapping("/{orderId}")
    @JsonView(PrivateSecondLevel.class)
    @Secured({"ROLE_ADMIN","ROLE_STORE_ADMIN","ROLE_COURIER"})
    public ResponseEntity<?> getOrder(@PathVariable @Min(1) long orderId){
        try{
            OrderJson orderJson = orderConverter.convertToModel(orderService.getOrderById(orderId));
            return new ResponseEntity<>(orderJson,HttpStatus.OK);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(new Status(e.getMessage()),HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/list/unassigned/page/{page}/count/{count}")
    @JsonView(Lists.class)
    @Secured({"ROLE_ADMIN","ROLE_COURIER"})
    public ResponseEntity<?> getUnassignedOrders(@PathVariable @Min(0) int page, @PathVariable @Min(1) int count){
        Page<Order> orders =orderService.getUnassignedOrders(page, count);
        if(orders.isEmpty()){
            return new ResponseEntity<>(new Status("There is no unassigned orders"),HttpStatus.OK);
        }
        OrderListJson orderListJson = orderConverter.convertToListModel(orders);
        return new ResponseEntity<>(orderListJson,HttpStatus.OK);
    }

    @GetMapping(value = "/list/page/{page}/count/{count}")
    @JsonView(Lists.class)
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> getOrders(@PathVariable @Min(0) int page, @PathVariable @Min(1) int count){
        Page<Order> orders = orderService.getOrders(page, count);
        if(orders.isEmpty()){
            return new ResponseEntity<>(new Status("There is no orders"),HttpStatus.OK);
        }
        OrderListJson orderListJson = orderConverter.convertToListModel(orders);
        return new ResponseEntity<>(orderListJson,HttpStatus.OK);
    }

    @PutMapping(value = "/{orderId}/updateStatus")
    @Secured({"ROLE_ADMIN","ROLE_COURIER"})
    public ResponseEntity<?>  updateOrderStatus(@PathVariable @Min(1) long orderId, @RequestBody @Valid StatusInfoJson statusInfoJson){
        try{
            orderService.updateOrderStatus(orderId,statusInfoJson);
            return new ResponseEntity<>(new Status("Order status updated"),HttpStatus.OK);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(new Status(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/list/mine/page/{page}/count/{count}")
    @JsonView(Lists.class)
    @Secured("ROLE_COURIER")
    public ResponseEntity<?> getCourierOrders(@PathVariable @Min(0) int page, @PathVariable @Min(1) int count){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User courier = userService.getUserByEmail(username);
            long courierId = courier.getId();
            Page<Order> courierOrders = orderService.getOrdersByCourierId(courierId,page, count);
            if(courierOrders.isEmpty()){
                return new ResponseEntity<>(new Status("There is no orders assigned to you"),HttpStatus.OK);
            }
            OrderListJson orderListJson = orderConverter.convertToListModel(courierOrders);
            return new ResponseEntity<>(orderListJson,HttpStatus.OK);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(new Status("Not found"),HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{orderId}/assignCourier/{courierId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?>  assignCourierToOrder(@PathVariable @Min(1) long orderId,@PathVariable @Min(1) long courierId){
        try{
            orderService.assignCourierToOrder(orderId,courierId);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(new Status(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Status("Order assigned to courier"),HttpStatus.OK);
    }

    @PutMapping(value = "/{orderId}/assignToMe")
    @Secured("ROLE_COURIER")
    public ResponseEntity<?>  assignOrder(@PathVariable @Min(1) long orderId){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User courier = userService.getUserByEmail(username);
            long courierId = courier.getId();
            orderService.assignCourierToOrder(orderId,courierId);
            return new ResponseEntity<>(new Status("Order assigned to you"),HttpStatus.OK);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(new Status(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }
}