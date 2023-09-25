package com.aca.acacourierservice.controller;

import com.aca.acacourierservice.converter.StoreConverter;
import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.*;
import com.aca.acacourierservice.service.OrderService;
import com.aca.acacourierservice.service.StoreService;
import com.aca.acacourierservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
public class StoreController {
    private final StoreService storeService;
    private final StoreConverter storeConverter;
    private final UserService userService;
    private final OrderService orderService;

    @Autowired
    public StoreController(StoreService storeService, StoreConverter storeConverter, UserService userService, OrderService orderService) {
        this.storeService = storeService;
        this.storeConverter = storeConverter;
        this.userService = userService;
        this.orderService = orderService;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/{storeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getStore(@PathVariable long storeId) {
        Store store;
        try {
            store = storeService.getStoreById(storeId);
            store.getAdmin().setPassword("Password hidden");
        } catch (CourierServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(storeConverter.convertToModel(store), HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<StoreJson> listStores(@RequestBody PageInfo pageInfo) {
        return storeService.listStoresByPage(pageInfo.getPage(), pageInfo.getCount());
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Status> registerStore(@RequestBody StoreJson storeJson) {
        try {
            User admin = storeJson.getAdmin();
            admin.setRole(User.Role.ROLE_STORE_ADMIN);
            userService.saveUser(admin);
            long id = storeService.addStore(storeJson);
            return new ResponseEntity<>(new StatusWithId("Store registered", id), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new Status(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured("ROLE_ADMIN")
    @PutMapping(value = "/{storeId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Status> updateStore(@RequestBody StoreJson storeJson, @PathVariable long storeId) {
        try {
            storeService.updateStore(storeId, storeJson);
        } catch (CourierServiceException e) {
            return new ResponseEntity<>(new Status(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new Status(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Status("Store updated"), HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping(value = "/{storeId}/admin/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Status> updateStoreAdmin(@RequestBody UserJson admin, @PathVariable long storeId) {
        Store store = storeService.getStoreById(storeId);
        admin.setRole(User.Role.ROLE_STORE_ADMIN);
        User adminEntity = store.getAdmin();
        try {
            userService.updateUser(admin, adminEntity);
        } catch (CourierServiceException e) {
            return new ResponseEntity<>(new Status(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new Status(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Status("Store admin updated"), HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping(value = "/{storeId}/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Status> deleteStore(@PathVariable long storeId) {
        try {
            storeService.deleteStoreById(storeId);
        } catch (CourierServiceException e) {
            return new ResponseEntity<>(new Status(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new Status(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Status("Store deleted"), HttpStatus.OK);
    }

    @Secured("ROLE_STORE_ADMIN")
    @GetMapping(value = "/{storeId}/order/list", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listOrders(@PathVariable long storeId, @RequestBody PageInfo pageInfo) {
        Page<Order> orderPage;
        try {
            orderPage = orderService.getOrdersByStoreId(storeId, pageInfo.getPage(), pageInfo.getCount());
        } catch (CourierServiceException e) {
            return new ResponseEntity<>(new Status(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new Status(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<Order> orders = orderPage.toList();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
