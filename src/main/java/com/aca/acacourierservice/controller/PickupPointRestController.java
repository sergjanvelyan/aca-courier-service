package com.aca.acacourierservice.controller;

import com.aca.acacourierservice.converter.PickupPointConverter;
import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.PageInfo;
import com.aca.acacourierservice.model.PickupPointJson;
import com.aca.acacourierservice.model.Status;
import com.aca.acacourierservice.model.StatusWithId;
import com.aca.acacourierservice.service.PickupPointService;
import com.aca.acacourierservice.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
    public ResponseEntity<Status> addPickupPoint(@RequestBody PickupPointJson pickupPointJson) {
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
    @Secured("ROLE_STORE_ADMIN")
    public ResponseEntity<?> getPickupPointById(@PathVariable Long pickupPointId) {
        try{
            PickupPoint pickupPoint = pickupPointService.getPickupPointById(pickupPointId);
            PickupPointJson pickupPointJson = pickupPointConverter.convertToModel(pickupPoint);
            return new ResponseEntity<>(pickupPointJson,HttpStatus.OK);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(new Status(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/list")
    @Secured("ROLE_STORE_ADMIN")
    public ResponseEntity<?> getPickupPointList(@RequestBody PageInfo pageInfo) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Store store = storeService.getStoreByAdminUsername(username);
            List<PickupPoint> pickupPointList = pickupPointService.getPickupPointsByStoreId(store.getId(),pageInfo.getPage(), pageInfo.getCount());
            List<PickupPointJson> pickupPointJsons = pickupPointConverter.convertToModelList(pickupPointList);
            return new ResponseEntity<>(pickupPointJsons,HttpStatus.OK);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(new Status(e.getMessage()),HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/{pickupPointId}")
    @Secured("ROLE_STORE_ADMIN")
    public ResponseEntity<Status> putPickupPointById(@PathVariable Long pickupPointId, @RequestBody PickupPointJson pickupPointJson) {
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
    public ResponseEntity<Status> deletePickupPointById(@PathVariable Long id) {
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