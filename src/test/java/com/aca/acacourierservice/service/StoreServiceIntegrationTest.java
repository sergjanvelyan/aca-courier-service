package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.StoreConverter;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.model.PickupPointJson;
import com.aca.acacourierservice.model.StoreJson;
import com.aca.acacourierservice.repository.PickupPointRepository;
import com.aca.acacourierservice.repository.StoreRepository;
import com.aca.acacourierservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class StoreServiceIntegrationTest {
    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreConverter storeConverter;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PickupPointRepository pickupPointRepository;

    @Test
    @Transactional
    public void testAddStore() {
        PickupPointJson pickupPoint = new PickupPointJson();
        pickupPoint.setCity("city");
        pickupPoint.setZipCode("5004");
        pickupPoint.setCountry("country");
        pickupPoint.setAddress("some address");
        pickupPoint.setPhoneNumber("+123456789");

        User admin = new User();
        admin.setId(1L);
        admin.setEmail("some@email.com");
        admin.setPassword("password123$");
        admin.setRole(User.Role.ROLE_STORE_ADMIN);
        userRepository.save(admin);


        StoreJson storeJson = new StoreJson();
        storeJson.setName("test store");
        storeJson.setStoreUrl("test.com");
        storeJson.setApiKey("apikey");
        storeJson.setApiSecret("apisecret");
        storeJson.setPhoneNumber("+123456789");
        storeJson.setAdmin(admin);

        List<PickupPointJson> pickupPoints = new ArrayList<>();
        pickupPoints.add(pickupPoint);
        storeJson.setPickupPoints(pickupPoints);

        long id = storeService.addStore(storeJson);

        Store savedStore = storeRepository.findById(id).orElse(null);

        assertNotNull(savedStore);
        assertEquals(savedStore.getName(), storeJson.getName());
        assertEquals(savedStore.getStoreUrl(), storeJson.getStoreUrl());
        assertEquals(savedStore.getApiKey(), storeJson.getApiKey());
        assertEquals(savedStore.getApiSecret(), storeJson.getApiSecret());
        assertEquals(savedStore.getPhoneNumber(), storeJson.getPhoneNumber());
        assertEquals(savedStore.getAdmin(), storeJson.getAdmin());
        //Todo:fix next assertion
        //assertEquals(savedStore.getPickupPoints().get(0), pickupPoint);

        StoreJson savedStoreJson = storeConverter.convertToModel(savedStore);
        assertEquals(savedStoreJson.getName(), storeJson.getName());
        assertEquals(savedStoreJson.getStoreUrl(), storeJson.getStoreUrl());
        assertEquals(savedStoreJson.getApiKey(), storeJson.getApiKey());
        assertEquals(savedStoreJson.getApiSecret(), storeJson.getApiSecret());
        assertEquals(savedStoreJson.getPhoneNumber(), storeJson.getPhoneNumber());
        assertEquals(savedStoreJson.getAdmin(), storeJson.getAdmin());
        //Todo:fix next assertion
        //assertEquals(savedStoreJson.getPickupPoints().get(0), storeJson.getPickupPoints().get(0));
    }
}
