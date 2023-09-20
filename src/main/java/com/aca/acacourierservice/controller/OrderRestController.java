package com.aca.acacourierservice.controller;

import com.aca.acacourierservice.converter.OrderConverter;
import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.*;
import com.aca.acacourierservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/order")
public class OrderRestController {
    private final OrderService orderService;
    private final OrderConverter orderConverter;
    @Autowired
    public OrderRestController(OrderService orderService, OrderConverter orderConverter) {
        this.orderService = orderService;
        this.orderConverter = orderConverter;
    }

    @PostMapping(value = "/create")
    public Status createOrder(@RequestBody OrderJson orderJson){
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
            return new ResponseEntity<>("There is no unassigned orders:",HttpStatus.OK);
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
        //TODO: there is additional work to do
        long courierId = 1L;
        Page<Order> courierOrders = orderService.getOrdersByCourierId(courierId,pageInfo.getPage(), pageInfo.getCount());
        if(courierOrders.isEmpty()){
            return new ResponseEntity<>("There is no orders assigned to you:",HttpStatus.OK);
        }
        OrderListJson orderListJson = orderConverter.convertToListModel(courierOrders);
        return new ResponseEntity<>(orderListJson,HttpStatus.OK);
    }

    @PutMapping(value = "/{orderId}/assignCourier")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?>  assignCourierToOrder(@PathVariable long orderId,@RequestParam long courierId){
        //TODO: implement courier selection logic
        try{
            orderService.assignCourierToOrder(orderId,courierId);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.OK);
        }
        return new ResponseEntity<>("Order(id="+orderId+") assigned to courier(id="+courierId+"):",HttpStatus.OK);
    }

    @PutMapping(value = "/{orderId}/assignToMe")
    @Secured("ROLE_COURIER")
    public ResponseEntity<?>  assignOrder(@PathVariable long orderId){
        //TODO: there is additional work to do
        long courierId = 1L;
        try{
            orderService.assignCourierToOrder(orderId,courierId);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.OK);
        }
        return new ResponseEntity<>("Order(id="+orderId+")+ assigned to you:",HttpStatus.OK);
    }
}