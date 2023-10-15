package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.StoreConverter;
import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.StoreJson;
import com.aca.acacourierservice.model.UserJson;
import com.aca.acacourierservice.repository.PickupPointRepository;
import com.aca.acacourierservice.repository.StoreRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class StoreService {
    private final PickupPointRepository pickupPointRepository;
    private final StoreRepository storeRepository;
    private final StoreConverter storeConverter;
    private final UserService userService;

    @Autowired
    public StoreService(PickupPointRepository pickupPointRepository, StoreRepository storeRepository, StoreConverter storeConverter, UserService userService) {
        this.pickupPointRepository = pickupPointRepository;
        this.storeRepository = storeRepository;
        this.storeConverter = storeConverter;
        this.userService = userService;
    }

    public Store getStoreById(@Min(1) long storeId) throws CourierServiceException {
        Optional<Store> storeOptional = storeRepository.findById(storeId);
        if (storeOptional.isEmpty()) {
            throw new CourierServiceException("There is no store with id=" + storeId + ":");
        }
        return storeOptional.get();
    }

    public Store getStoreByApiKey(String apiKey) throws CourierServiceException {
        Optional<Store> storeOptional = storeRepository.findByApiKey(apiKey);
        if (storeOptional.isEmpty()) {
            throw new CourierServiceException("There is no store with apiKey=" + apiKey + ":");
        }
        return storeOptional.get();
    }

    public Store getStoreByAdminUsername(@Email String email) throws CourierServiceException {
        Optional<Store> storeOptional = storeRepository.findByAdmin_Email(email);
        if (storeOptional.isEmpty()) {
            throw new CourierServiceException("There is no store with admin username=" + email + ":");
        }
        return storeOptional.get();
    }

    @Transactional
    public long addStore(@Valid StoreJson storeJson) {
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
    public Store addStoreAndAdmin(@Valid StoreJson storeJson) throws NoSuchAlgorithmException {
        Store store = storeConverter.convertToEntity(storeJson);
        store.setApiKey(generateKey(256));
        store.setApiSecret(generateKey(256));

        if (store.getPickupPoints() != null) {
            for (PickupPoint pickupPoint : store.getPickupPoints()) {
                pickupPoint.setStore(store);
                pickupPointRepository.save(pickupPoint);
            }
        }
        User admin = storeJson.getAdmin();
        admin.setRole(User.Role.ROLE_STORE_ADMIN);
        userService.saveUser(admin);
        storeRepository.save(store);
        return store;
    }

    @Transactional
    public void updateStore(@Min(1) long id, @Valid StoreJson storeJson) throws CourierServiceException {
        Store store = getStoreById(id);
        store=storeConverter.convertToEntity(storeJson,store);
        storeRepository.save(store);
    }

    @Transactional
    public User changeStoreAdmin(@Valid UserJson admin, @Min(1) long storeId) throws CourierServiceException {
        Store store = getStoreById(storeId);
        admin.setRole(User.Role.ROLE_STORE_ADMIN);
        store.setAdmin(userService.saveUser(admin));
        storeRepository.save(store);
        return store.getAdmin();
    }

    public List<StoreJson> listStoresByPage(@Min(0) int page, @Min(1) int count) {
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
    public void deleteStoreById(@Min(1) long id) throws CourierServiceException {
        if (!storeRepository.existsById(id)) {
            throw new CourierServiceException("There is no store");
        }
        storeRepository.deleteById(id);
    }

    public String generateKey(int length) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(length);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] rawData = secretKey.getEncoded();
        return Base64.getEncoder().encodeToString(rawData);
    }
}