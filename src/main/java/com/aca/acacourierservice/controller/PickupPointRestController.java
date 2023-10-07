package com.aca.acacourierservice.controller;

import com.aca.acacourierservice.converter.PickupPointConverter;
import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.PickupPointJson;
import com.aca.acacourierservice.model.Status;
import com.aca.acacourierservice.model.StatusWithId;
import com.aca.acacourierservice.service.PickupPointService;
import com.aca.acacourierservice.service.StoreService;
import com.aca.acacourierservice.validation.OnCreate;
import com.aca.acacourierservice.validation.OnUpdate;
import com.aca.acacourierservice.view.Lists;
import com.aca.acacourierservice.view.PrivateSecondLevel;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/pickupPoint")
public class PickupPointRestController {
    private final PickupPointService pickupPointService;
    private final PickupPointConverter pickupPointConverter;
    private final StoreService storeService;
    @Autowired
    public PickupPointRestController(PickupPointService pickupPointService, PickupPointConverter pickupPointConverter, StoreService storeService) {
        this.pickupPointService = pickupPointService;
        this.pickupPointConverter = pickupPointConverter;
        this.storeService = storeService;
    }
    @PostMapping("/add")
    @Secured("ROLE_STORE_ADMIN")
    @Validated(OnCreate.class)
    public ResponseEntity<Status> addPickupPoint(@RequestBody @Valid PickupPointJson pickupPointJson) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Store store = storeService.getStoreByAdminUsername(username);
            long storeId = store.getId();
            pickupPointJson.setStoreId(storeId);
            long pickupPointId = pickupPointService.addPickupPoint(pickupPointJson).getId();
            return  new ResponseEntity<>(new StatusWithId("Created",pickupPointId),HttpStatus.OK);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(new Status(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{pickupPointId}")
    @JsonView(PrivateSecondLevel.class)
    @Validated
    @Secured("ROLE_STORE_ADMIN")
    public ResponseEntity<?> getPickupPointById(@PathVariable @Min(1) Long pickupPointId) {
        PickupPoint pickupPoint;
        Store store;
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            store = storeService.getStoreByAdminUsername(username);
            pickupPoint = pickupPointService.getPickupPointById(pickupPointId);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(new Status(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        if(!store.getId().equals(pickupPoint.getStore().getId())){
            return new ResponseEntity<>(new Status("Pickup point for this store not found"),HttpStatus.BAD_REQUEST);
        }
        PickupPointJson pickupPointJson = pickupPointConverter.convertToModel(pickupPoint);
        return new ResponseEntity<>(pickupPointJson,HttpStatus.OK);
    }
    @GetMapping("/list/page/{page}/count/{count}")
    @JsonView(Lists.class)
    @Validated
    @Secured("ROLE_STORE_ADMIN")
    public ResponseEntity<?> getPickupPointList(@PathVariable @Min(0) int page, @PathVariable @Min(1) int count) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Store store = storeService.getStoreByAdminUsername(username);
            List<PickupPoint> pickupPointList = pickupPointService.getPickupPointsByStoreId(store.getId(),page, count);
            List<PickupPointJson> pickupPointJsons = pickupPointConverter.convertToModelList(pickupPointList);
            return new ResponseEntity<>(pickupPointJsons,HttpStatus.OK);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(new Status(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/{pickupPointId}")
    @Validated(OnUpdate.class)
    @Secured("ROLE_STORE_ADMIN")
    public ResponseEntity<Status> updatePickupPointById(@PathVariable @Min(value = 1,groups = OnUpdate.class) Long pickupPointId, @RequestBody @Valid PickupPointJson pickupPointJson) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            pickupPointService.modifyPickupPoint(pickupPointId,username,pickupPointJson);
            return new ResponseEntity<>(new Status("Pickup point updated"),HttpStatus.OK);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(new Status(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/{id}")
    @Secured("ROLE_STORE_ADMIN")
    @Validated
    public ResponseEntity<Status> deletePickupPointById(@PathVariable @Min(1) Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            pickupPointService.deletePickupPoint(id,username);
            return new ResponseEntity<>(new Status("Pickup point deleted"), HttpStatus.OK);
        } catch (CourierServiceException e) {
            return new ResponseEntity<>(new Status(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }
}