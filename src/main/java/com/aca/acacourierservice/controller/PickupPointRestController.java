package com.aca.acacourierservice.controller;

import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.PickupPointJson;
import com.aca.acacourierservice.model.Status;
import com.aca.acacourierservice.service.PickupPointService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("pickupPoint")
public class PickupPointRestController {
    private final PickupPointService pickupPointService;

    public PickupPointRestController(PickupPointService pickupPointService) {
        this.pickupPointService = pickupPointService;
    }

    @PostMapping("add")
    public ResponseEntity<Status> addPickupPoint(@RequestBody PickupPointJson pickupPointJson) {
        pickupPointService.addPickupPoint(pickupPointJson);
        return ResponseEntity.ok().body(new Status("Created"));
    }

    @GetMapping("{pickupPointId}")
    public ResponseEntity<PickupPoint> getPickupPointById(@PathVariable Long pickupPointId) {
        return ResponseEntity.ok().body(pickupPointService.getPickupPointById(pickupPointId));
    }

    @GetMapping("list")
    public ResponseEntity<List<PickupPointJson>> getPickupPointList() {
        return ResponseEntity.ok().body(pickupPointService.findAll());
    }

    @PutMapping("{pickupPointId}")
    public ResponseEntity<PickupPoint> putPickupPointById(@PathVariable Long pickupPointId,
                                                          @RequestBody PickupPointJson pickupPointJson) {
        return ResponseEntity.ok().body(pickupPointService.modifyPickupPoint(pickupPointId, pickupPointJson));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePickupPointById(@PathVariable Long id) {
        try {
            pickupPointService.deletePickupPoint(id);
        } catch (CourierServiceException e) {
            return new ResponseEntity<>("There is no pickup point with id", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }
}