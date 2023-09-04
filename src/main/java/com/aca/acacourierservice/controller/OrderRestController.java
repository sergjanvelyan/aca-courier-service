package com.aca.acacourierservice.controller;

import com.aca.acacourierservice.converter.OrderConverter;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.OrderJson;
import com.aca.acacourierservice.model.Status;
import com.aca.acacourierservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<?> getUnassignedOrders(){
        //TODO: add body
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //  /list GET (admin)
    @GetMapping(value = "/list")
    public ResponseEntity<?> getOrders(){
        //TODO: add body
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //  /list/mine GET (Courier)
    @GetMapping(value = "/list/mine")
    public ResponseEntity<?> getCourierOrders(){
        //TODO: add body
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //  /updateStatus/ PUT {Courier, Admin}
    @PutMapping(value = "/updateStatus")
    public ResponseEntity<?>  updateOrderStatus(){
        //TODO: add body
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //  /{orderId}/assignCourier PUT (Admin)
    @PutMapping(value = "/{orderId}/assignCourier")
    public ResponseEntity<?>  assignCourierToOrder(@PathVariable long orderId){
        //TODO: add body
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //  /{orderId}/assignToMe PUT (courier)
    @PutMapping(value = "/{orderId}/assignToMe")
    public ResponseEntity<?>  assignOrder(@PathVariable long orderId){
        //TODO: add body
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
