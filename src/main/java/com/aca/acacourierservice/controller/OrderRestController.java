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
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
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
    public ResponseEntity<String> exceptionHandler(InvalidStoreCredentialsException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/create")
    @ValidateApiKeySecret
    public Status createOrder(@RequestBody OrderJson orderJson,HttpServletRequest request){
        orderJson.setStoreId((Long) request.getAttribute("storeId"));
        long id = orderService.addOrder(orderJson);
        return new Status("Created order id="+id+":");
    }

    @GetMapping("/{orderId}")
    @Secured({"ROLE_ADMIN","ROLE_STORE_ADMIN","ROLE_COURIER"})
    public ResponseEntity<?> getOrder(@PathVariable long orderId){
        try{
            OrderJson orderJson = orderConverter.convertToModel(orderService.getOrderById(orderId));
            return new ResponseEntity<>(orderJson,HttpStatus.OK);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/list/unassigned")
    @Secured({"ROLE_ADMIN","ROLE_COURIER"})
    public ResponseEntity<?> getUnassignedOrders(@RequestBody PageInfo pageInfo){
        Page<Order> orders =orderService.getUnassignedOrders(pageInfo.getPage(), pageInfo.getCount());
        if(orders.isEmpty()){
            return new ResponseEntity<>("There is no unassigned orders:",HttpStatus.NO_CONTENT);
        }
        OrderListJson orderListJson = orderConverter.convertToListModel(orders);
        return new ResponseEntity<>(orderListJson,HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> getOrders(@RequestBody PageInfo pageInfo){
        Page<Order> orders = orderService.getOrders(pageInfo.getPage(), pageInfo.getCount());
        if(orders.isEmpty()){
            return new ResponseEntity<>("There is no orders:",HttpStatus.NO_CONTENT);
        }
        OrderListJson orderListJson = orderConverter.convertToListModel(orders);
        return new ResponseEntity<>(orderListJson,HttpStatus.OK);
    }

    @PutMapping(value = "/{orderId}/updateStatus")
    @Secured({"ROLE_ADMIN","ROLE_COURIER"})
    public ResponseEntity<?>  updateOrderStatus(@PathVariable long orderId, @RequestBody StatusInfoJson statusInfoJson){
        try{
            orderService.updateOrderStatus(orderId,statusInfoJson.getStatus(),statusInfoJson.getAdditionalInfo());
            return new ResponseEntity<>("Order(id="+orderId+") status updated:",HttpStatus.OK);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value = "/list/mine")
    @Secured("ROLE_COURIER")
    public ResponseEntity<?> getCourierOrders(@RequestBody PageInfo pageInfo){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User courier = userService.getUserByEmail(username);
            long courierId = courier.getId();
            Page<Order> courierOrders = orderService.getOrdersByCourierId(courierId,pageInfo.getPage(), pageInfo.getCount());
            if(courierOrders.isEmpty()){
                return new ResponseEntity<>("There is no orders assigned to you:",HttpStatus.NO_CONTENT);
            }
            OrderListJson orderListJson = orderConverter.convertToListModel(courierOrders);
            return new ResponseEntity<>(orderListJson,HttpStatus.OK);
        }catch (CourierServiceException e){
            return new ResponseEntity<>("Not found",HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{orderId}/assignCourier")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?>  assignCourierToOrder(@PathVariable long orderId,@RequestParam long courierId){
        //TODO: implement courier selection logic
        try{
            orderService.assignCourierToOrder(orderId,courierId);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Order(id="+orderId+") assigned to courier(id="+courierId+"):",HttpStatus.OK);
    }

    @PutMapping(value = "/{orderId}/assignToMe")
    @Secured("ROLE_COURIER")
    public ResponseEntity<?>  assignOrder(@PathVariable long orderId){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User courier = userService.getUserByEmail(username);
            long courierId = courier.getId();
            orderService.assignCourierToOrder(orderId,courierId);
            return new ResponseEntity<>("Order(id="+orderId+") assigned to you:",HttpStatus.OK);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.OK);
        }
    }
}