package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.PickupPointConverter;
import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.PickupPointJson;
import com.aca.acacourierservice.repository.PickupPointRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class PickupPointService {
    private final PickupPointRepository pickupPointRepository;
    private final PickupPointConverter pickupPointConverter;
    private final StoreService storeService;

    @Autowired
    public PickupPointService(PickupPointRepository pickupPointRepository, PickupPointConverter pickupPointConverter, StoreService storeService) {
        this.pickupPointRepository = pickupPointRepository;
        this.pickupPointConverter = pickupPointConverter;
        this.storeService = storeService;
    }
    public PickupPoint getPickupPointById(@Min(1) long id) throws CourierServiceException {
        Optional<PickupPoint> pickupPointOptional = pickupPointRepository.findById(id);
        if (pickupPointOptional.isEmpty()) {
            throw new CourierServiceException("Pickup point not found");
        }
        return pickupPointOptional.get();
    }
    public List<PickupPoint> getPickupPointsByStoreId(@Min(1) long storeId,@Min(0) int page,@Min(1) int size) throws CourierServiceException {
        Page<PickupPoint> pickupPointsPage = pickupPointRepository.findAllByStoreId(storeId, PageRequest.of(page, size));
        return pickupPointsPage.getContent();
    }
    public void modifyPickupPoint(@Min(1) long id,@Email String email,@Valid PickupPointJson pickupPointJson) throws CourierServiceException {
        Optional<PickupPoint> pickupPointOptional = pickupPointRepository.findByIdAndStore_Admin_Email(id,email);
        if (pickupPointOptional.isEmpty()) {
            throw new CourierServiceException("Pickup point for this store not found");
        }
        PickupPoint pickupPoint = pickupPointOptional.get();
        pickupPoint = pickupPointConverter.convertToEntity(pickupPointJson, pickupPoint);
        pickupPointRepository.save(pickupPoint);
    }

    @Transactional
    public PickupPoint addPickupPoint(@Valid PickupPointJson pickupPointJson) throws CourierServiceException{
        PickupPoint pickupPoint = pickupPointConverter.convertToEntity(pickupPointJson);
        pickupPoint.setStore(storeService.getStoreById(pickupPointJson.getStoreId()));
        return pickupPointRepository.save(pickupPoint);
    }
    @Transactional
    public void deletePickupPoint(@Min(1) long id,@Email String email) throws CourierServiceException {
        if (!pickupPointRepository.existsByIdAndStore_Admin_Email(id,email)) {
            throw new CourierServiceException("Pickup point for this store not found");
        }
        pickupPointRepository.deleteById(id);
    }
}