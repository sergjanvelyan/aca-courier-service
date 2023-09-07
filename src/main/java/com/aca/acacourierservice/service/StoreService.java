package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.StoreConverter;
import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.StoreJson;
import com.aca.acacourierservice.repository.PickupPointRepository;
import com.aca.acacourierservice.repository.StoreRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StoreService {
    private final PickupPointRepository pickupPointRepository;
    private final StoreRepository storeRepository;
    private final StoreConverter storeConverter;

    @Autowired
    public StoreService(PickupPointRepository pickupPointRepository, StoreRepository storeRepository, StoreConverter storeConverter) {
        this.pickupPointRepository = pickupPointRepository;
        this.storeRepository = storeRepository;
        this.storeConverter = storeConverter;
    }

    public Store getStoreById(long storeId) {
        Optional<Store> storeOptional = storeRepository.findById(storeId);
        if (storeOptional.isEmpty()) {
            throw new CourierServiceException("There is no store with id " + storeId);
        }
        return storeOptional.get();
    }

    //Set ROLE_ADMIN access on this method
    @Transactional
    public long addStore(StoreJson storeJson) {
        Store store = storeConverter.convertToEntity(storeJson);
        if (store.getPickupPoints() != null) {
            for (PickupPoint pickupPoint : store.getPickupPoints()) {
                pickupPoint.setStore(store);
                pickupPointRepository.save(pickupPoint);
            }
        }
        storeRepository.save(store);
        return store.getId();
    }

    @Transactional
    public void updateStore(long id, StoreJson storeJson) {
        Store store = getStoreById(id);
        if (storeJson.getName() != null) {
            store.setName(storeJson.getName());
        }
        if (storeJson.getStoreUrl() != null) {
            store.setStoreUrl(storeJson.getStoreUrl());
        }
        if (storeJson.getPhoneNumber() != null) {
            store.setPhoneNumber(storeJson.getPhoneNumber());
        }
        if (storeJson.getAdmin() != null) {
            store.setAdmin(storeJson.getAdmin());
        }
        if (storeJson.getPickupPoints() != null) {
            store.setPickupPoints(storeJson.getPickupPoints());
        }
        storeRepository.save(store);
    }

    //Set ROLE_STORE_ADMIN access on this method
    @Transactional
    public void addPickupPoints(long id, List<PickupPoint> pickupPoints) {
        Store store = getStoreById(id);
        store.setPickupPoints(pickupPoints);
        storeRepository.save(store);
    }

    public List<StoreJson> listStoresByPage(int page, int count) {
        Page<Store> storePage = storeRepository.findAll(PageRequest.of(page, count));
        List<StoreJson> stores = new ArrayList<>();
        for (Store store : storePage) {
            StoreJson storeJson = storeConverter.convertToModel(store);
            stores.add(storeJson);
        }
        return stores;
    }
}