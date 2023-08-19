package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.StoreConverter;
import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.exception.StoreServiceException;
import com.aca.acacourierservice.model.StoreJson;
import com.aca.acacourierservice.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final StoreConverter storeConverter;

    @Autowired
    public StoreService(StoreRepository storeRepository, StoreConverter storeConverter) {
        this.storeRepository = storeRepository;
        this.storeConverter = storeConverter;
    }

    public Store getStoreById(long storeId) {
        Optional<Store> storeOptional = storeRepository.findById(storeId);
        if (storeOptional.isEmpty()) {
            throw new StoreServiceException("There is no store with id " + storeId);
        }
        return storeOptional.get();
    }

    //Set ROLE_ADMIN access on this method
    public long addStore(StoreJson storeJson) {
        Store store = storeConverter.convertToEntity(storeJson, new Store());

        storeRepository.save(store);
        return store.getId();
    }

    public void updateStore(long id, StoreJson storeJson) {
        Store store = getStoreById(id);
        store = storeConverter.convertToEntity(storeJson, store);
        storeRepository.save(store);
    }

    //Set ROLE_STORE_ADMIN access on this method
    //Change pickupPoints generic type to PickupPointJson after writing converters for it
    public void addPickupPoints(long id, List<PickupPoint> pickupPoints) {
        Store store = getStoreById(id);
        store.setPickupPoints(pickupPoints);
        storeRepository.save(store);
    }
}
