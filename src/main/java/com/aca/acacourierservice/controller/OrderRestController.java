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

    //  /create POST (ALL)
    @PostMapping(value = "/create")
    public Status createOrder(@RequestBody OrderJson orderJson){
        long id = orderService.addOrder(orderJson);
        return new Status("Created order id="+id);
    }

    //  /{orderId} GET (Courier, ADMIN, SToreAdmin )
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable long orderId){
        try{
            OrderJson orderJson = orderConverter.convertToModel(orderService.getOrderById(orderId));
            return new ResponseEntity<>(orderJson,HttpStatus.OK);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    //  /list/unassigned GET (Courier, admin)
    @GetMapping(value = "/list/unassigned")
    public ResponseEntity<?> getUnassignedOrders(@RequestBody PageInfo pageInfo){
        Page<Order> orders =orderService.getUnassignedOrders(pageInfo.getPage(), pageInfo.getCount());
        if(orders.isEmpty()){
            return new ResponseEntity<>("There is no unassigned orders",HttpStatus.OK);
        }
        OrderListJson orderListJson = orderConverter.convertToListModel(orders);
        return new ResponseEntity<>(orderListJson,HttpStatus.OK);
    }

    //  /list GET (admin)
    @GetMapping(value = "/list")
    public ResponseEntity<?> getOrders(@RequestBody PageInfo pageInfo){
        Page<Order> orders = orderService.getOrders(pageInfo.getPage(), pageInfo.getCount());
        if(orders.isEmpty()){
            return new ResponseEntity<>("There is no orders",HttpStatus.NO_CONTENT);
        }
        OrderListJson orderListJson = orderConverter.convertToListModel(orders);
        return new ResponseEntity<>(orderListJson,HttpStatus.OK);
    }

    //  /updateStatus/ PUT {Courier, Admin}
    @PutMapping(value = "/{orderId}/updateStatus")
    public ResponseEntity<?>  updateOrderStatus(@PathVariable long orderId, @RequestBody StatusInfoJson statusInfoJson){
        try{
            orderService.updateOrderStatus(orderId,statusInfoJson.getStatus(),statusInfoJson.getAdditionalInfo());
            return new ResponseEntity<>("Status updated",HttpStatus.OK);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NO_CONTENT);
        }
    }

    //  /list/mine GET (Courier)
    @GetMapping(value = "/list/mine")
    public ResponseEntity<?> getCourierOrders(@RequestBody PageInfo pageInfo){
        //TODO: there is additional work to do
        long courierId = 1L;
        Page<Order> courierOrders = orderService.getOrdersByCourierId(courierId,pageInfo.getPage(), pageInfo.getCount());
        if(courierOrders.isEmpty()){
            return new ResponseEntity<>("There is no orders assigned to you",HttpStatus.OK);
        }
        OrderListJson orderListJson = orderConverter.convertToListModel(courierOrders);
        return new ResponseEntity<>(orderListJson,HttpStatus.OK);
    }

    //  /{orderId}/assignCourier PUT (Admin)
    @PutMapping(value = "/{orderId}/assignCourier")
    public ResponseEntity<?>  assignCourierToOrder(@PathVariable long orderId,@RequestParam long courierId){
        //TODO: implement courier selection logic
        try{
            orderService.assignCourierToOrder(orderId,courierId);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.OK);
        }
        return new ResponseEntity<>("Order(id="+orderId+")+ assigned to courier(id="+courierId+")",HttpStatus.OK);
    }

    //  /{orderId}/assignToMe PUT (courier)
    @PutMapping(value = "/{orderId}/assignToMe")
    public ResponseEntity<?>  assignOrder(@PathVariable long orderId){
        //TODO: there is additional work to do
        long courierId = 1L;
        try{
            orderService.assignCourierToOrder(orderId,courierId);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.OK);
        }
        return new ResponseEntity<>("Order(id="+orderId+")+ assigned to you",HttpStatus.OK);
    }
}