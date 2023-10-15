package com.aca.acacourierservice.controller;

import com.aca.acacourierservice.converter.OrderConverter;
import com.aca.acacourierservice.converter.StoreConverter;
import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.*;
import com.aca.acacourierservice.service.OrderService;
import com.aca.acacourierservice.service.StoreService;
import com.aca.acacourierservice.validation.OnCreate;
import com.aca.acacourierservice.validation.OnUpdate;
import com.aca.acacourierservice.view.Lists;
import com.aca.acacourierservice.view.PrivateSecondLevel;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
@Validated
public class StoreController {
    private final StoreService storeService;
    private final StoreConverter storeConverter;
    private final OrderService orderService;
    private final OrderConverter orderConverter;

    @Autowired
    public StoreController(StoreService storeService, StoreConverter storeConverter, OrderService orderService, OrderConverter orderConverter) {
        this.storeService = storeService;
        this.storeConverter = storeConverter;
        this.orderService = orderService;
        this.orderConverter = orderConverter;
    }

    @Secured("ROLE_ADMIN")
    @JsonView(PrivateSecondLevel.class)
    @GetMapping(value = "/{storeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getStore(@PathVariable @Min(1) long storeId) {
        Store store;
        try {
            store = storeService.getStoreById(storeId);
            store.getAdmin().setPassword("Password hidden");
            StoreJson storeJson = storeConverter.convertToModel(store);
            return new ResponseEntity<>(storeJson, HttpStatus.OK);
        } catch (CourierServiceException e) {
            return new ResponseEntity<>(new Status(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new Status(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured("ROLE_ADMIN")
    @JsonView(Lists.class)
    @GetMapping(value = "/list/page/{page}/count/{count}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StoreJson> listStores(@PathVariable @Min(0) int page, @PathVariable @Min(1) int count) {
        return storeService.listStoresByPage(page, count);
    }

    @Secured("ROLE_ADMIN")
    @Validated(OnCreate.class)
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Status> registerStore(@RequestBody @Valid StoreJson storeJson) {
        try {
            Store store = storeService.addStoreAndAdmin(storeJson);
            return new ResponseEntity<>(new StatusWithKeyAndSecret("Store registered", store.getId(), store.getApiKey(), store.getApiSecret()), HttpStatus.CREATED);
        } catch (Exception e) {
            if (e.getMessage().contains("duplicate key value violates unique constraint")) {
                return new ResponseEntity<>(new Status("Duplicate api key or api secret"), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new Status(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured("ROLE_ADMIN")
    @Validated(OnUpdate.class)
    @PutMapping(value = "/{storeId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Status> updateStore(@RequestBody @Valid StoreJson storeJson, @PathVariable @Min(1) long storeId) {
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
    public ResponseEntity<Status> updateStoreAdmin(@RequestBody @Valid UserJson admin, @PathVariable @Min(1) long storeId) {
        try {
            long adminId = storeService.changeStoreAdmin(admin, storeId).getId();
            return new ResponseEntity<>(new StatusWithId("Store admin updated", adminId), HttpStatus.OK);
        } catch (CourierServiceException e) {
            return new ResponseEntity<>(new Status(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new Status(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping(value = "/{storeId}/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Status> deleteStore(@PathVariable @Min(1) long storeId) {
        try {
            storeService.deleteStoreById(storeId);
        } catch (CourierServiceException e) {
            return new ResponseEntity<>(new StatusWithId(e.getMessage(), storeId), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new Status(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Status("Store deleted"), HttpStatus.OK);
    }

    @Secured("ROLE_STORE_ADMIN")
    @JsonView(Lists.class)
    @GetMapping(value = "/{storeId}/order/list/page/{page}/count/{count}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listOrders(@PathVariable @Min(1) long storeId, @PathVariable @Min(0) int page, @PathVariable @Min(1) int count) {
        Page<Order> orderPage;
        try {
            orderPage = orderService.getOrdersByStoreId(storeId, page, count);
        } catch (Exception e) {
            return new ResponseEntity<>(new Status(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        OrderListJson orders = orderConverter.convertToListModel(orderPage);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
