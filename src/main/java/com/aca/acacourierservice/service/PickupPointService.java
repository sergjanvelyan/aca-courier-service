package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.PickupPointConverter;
import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.PickupPointJson;
import com.aca.acacourierservice.repository.PickupPointRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PickupPointService {
    private final PickupPointRepository pickupPointRepository;
    private final PickupPointConverter pickupPointConverter;

    @Autowired
    public PickupPointService(PickupPointRepository pickupPointRepository, PickupPointConverter pickupPointConverter) {
        this.pickupPointRepository = pickupPointRepository;
        this.pickupPointConverter = pickupPointConverter;
    }
    public PickupPoint getPickupPointById(long id){
        Optional<PickupPoint> pickupPointOptional = pickupPointRepository.findById(id);
        if (pickupPointOptional.isEmpty()){
            throw new CourierServiceException("There is no pickup point with id("+id+")");
        }
        return pickupPointOptional.get();
    }
    @Transactional
    public long addPickupPoint(PickupPointJson pickupPointJson) {
        PickupPoint pickupPoint = pickupPointConverter.convertToEntity(pickupPointJson);
        pickupPointRepository.save(pickupPoint);
        return pickupPoint.getId();
    }
    @Transactional
    public void deletePickupPoint(long id){
        if(!pickupPointRepository.existsById(id)){
            throw new CourierServiceException("There is no pickup point with id("+id+")");
        }
        pickupPointRepository.deleteById(id);
    }
}