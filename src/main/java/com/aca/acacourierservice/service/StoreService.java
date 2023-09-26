package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.PickupPointConverter;
import com.aca.acacourierservice.converter.StoreConverter;
import com.aca.acacourierservice.converter.UserConverter;
import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.StoreJson;
import com.aca.acacourierservice.model.UserJson;
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
    private final UserService userService;
    private final UserConverter userConverter;
    private final PickupPointConverter pickupPointConverter;

    @Autowired
    public StoreService(PickupPointRepository pickupPointRepository, StoreRepository storeRepository, StoreConverter storeConverter, UserService userService, UserConverter userConverter, PickupPointConverter pickupPointConverter) {
        this.pickupPointRepository = pickupPointRepository;
        this.storeRepository = storeRepository;
        this.storeConverter = storeConverter;
        this.userService = userService;
        this.userConverter = userConverter;
        this.pickupPointConverter = pickupPointConverter;
    }

    public Store getStoreById(long storeId) throws CourierServiceException {
        Optional<Store> storeOptional = storeRepository.findById(storeId);
        if (storeOptional.isEmpty()) {
            throw new CourierServiceException("There is no store with id=" + storeId+":");
        }
        return storeOptional.get();
    }
    public Store getStoreByApiKey(String apiKey) throws CourierServiceException {
        Optional<Store> storeOptional = storeRepository.findByApiKey(apiKey);
        if (storeOptional.isEmpty()) {
            throw new CourierServiceException("There is no store with apiKey=" + apiKey+":");
        }
        return storeOptional.get();
    }
    public Store getStoreByAdminUsername(String email) throws CourierServiceException {
        Optional<Store> storeOptional = storeRepository.findByAdmin_Email(email);
        if (storeOptional.isEmpty()) {
            throw new CourierServiceException("There is no store with admin username=" + email+":");
        }
        return storeOptional.get();
    }

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
    public void updateStore(long id, StoreJson storeJson) throws CourierServiceException {
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
            UserJson newStoreAdminJson = userConverter.convertToModel(storeJson.getAdmin());
            newStoreAdminJson.setRole(User.Role.ROLE_STORE_ADMIN);
            User storeAdmin = store.getAdmin();
            userService.updateUser(newStoreAdminJson,storeAdmin);
            store.setAdmin(storeAdmin);
        }
        if (storeJson.getPickupPoints() != null) {
            store.setPickupPoints(pickupPointConverter.convertToEntityList(storeJson.getPickupPoints()));
        }
        storeRepository.save(store);
    }

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
            storeJson.getAdmin().setPassword("Password hidden");
            stores.add(storeJson);
        }
        return stores;
    }

    @Transactional
    public void deleteStoreById(long id) throws CourierServiceException {
        if (!storeRepository.existsById(id)) {
            throw new CourierServiceException("There is no store with id=" + id+":");
        }
        storeRepository.deleteById(id);
    }
}