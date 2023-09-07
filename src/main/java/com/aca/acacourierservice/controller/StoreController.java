package com.aca.acacourierservice.controller;

import com.aca.acacourierservice.converter.StoreConverter;
import com.aca.acacourierservice.converter.UserConverter;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.*;
import com.aca.acacourierservice.service.StoreService;
import com.aca.acacourierservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
public class StoreController {
    private final StoreService storeService;
    private final StoreConverter storeConverter;
    private final UserService userService;
    private final UserConverter userConverter;

    @Autowired
    public StoreController(StoreService storeService, StoreConverter storeConverter, UserService userService, UserConverter userConverter) {
        this.storeService = storeService;
        this.storeConverter = storeConverter;
        this.userService = userService;
        this.userConverter = userConverter;
    }

    @GetMapping(value = "/{storeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getStore(@PathVariable long storeId) {
        Store store;
        try {
            store = storeService.getStoreById(storeId);
        } catch (CourierServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(storeConverter.convertToModel(store), HttpStatus.OK);
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<StoreJson> listStores(@RequestBody PageInfo pageInfo) {
        return storeService.listStoresByPage(pageInfo.getPage(), pageInfo.getCount());
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Status> registerStore(@RequestBody StoreJson storeJson) {
        User admin = storeJson.getAdmin();
        userService.saveUser(admin);
        try {
            long id = storeService.addStore(storeJson);
            return new ResponseEntity<>(new StatusWithId("created", id), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new Status(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{storeId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Status> updateStore(@RequestBody StoreJson storeJson, @PathVariable long storeId) {
        try {
            storeService.updateStore(storeId, storeJson);
        } catch (CourierServiceException e) {
            return new ResponseEntity<>(new Status(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new Status(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Status("updated"), HttpStatus.OK);
    }

    @PutMapping(value = "/{storeId}/admin/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Status> updateStoreAdmin(@RequestBody UserJson admin, @PathVariable long storeId) {
        Store store = storeService.getStoreById(storeId);
        User adminEntity = store.getAdmin();
        try {
            userService.updateUser(admin, adminEntity.getId());
        } catch (CourierServiceException e) {
            return new ResponseEntity<>(new Status(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new Status(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Status("updated"), HttpStatus.OK);
    }
}
