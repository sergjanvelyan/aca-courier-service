package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.StoreConverter;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.model.PickupPointJson;
import com.aca.acacourierservice.model.StoreJson;
import com.aca.acacourierservice.model.UserJson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class StoreServiceIntegrationTest {
    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreConverter storeConverter;

    @Test
    public void testAddStore() throws NoSuchAlgorithmException {
        PickupPointJson pickupPoint = new PickupPointJson();
        pickupPoint.setCity("City");
        pickupPoint.setZipCode("5004");
        pickupPoint.setCountry("Country");
        pickupPoint.setAddress("Address 45");
        pickupPoint.setPhoneNumber("+37499856425");
        List<PickupPointJson> pickupPointJsonList = List.of(pickupPoint);

        UserJson admin = new UserJson();
        admin.setEmail("admin@email.com");
        admin.setPassword("admin12345");
        admin.setFullName("FirstName LastName");
        admin.setPhoneNumber("+37455698564");
        admin.setBirthDate("1995-12-10");

        StoreJson storeJson = new StoreJson();
        storeJson.setName("Test store");
        storeJson.setStoreUrl("Test.com");
        storeJson.setPhoneNumber("+37498654523");
        storeJson.setAdmin(admin);
        storeJson.setPickupPoints(pickupPointJsonList);

        Store savedStore = storeService.addStoreAndAdmin(storeJson);

        assertNotNull(savedStore);
        assertEquals(savedStore.getName(), storeJson.getName());
        assertEquals(savedStore.getStoreUrl(), storeJson.getStoreUrl());
        assertEquals(savedStore.getPhoneNumber(), storeJson.getPhoneNumber());
        assertNotNull(savedStore.getAdmin());
        assertNotNull(savedStore.getPickupPoints());

        StoreJson savedStoreJson = storeConverter.convertToModel(savedStore);
        assertEquals(savedStoreJson.getName(), savedStore.getName());
        assertEquals(savedStoreJson.getStoreUrl(), savedStore.getStoreUrl());
        assertEquals(savedStoreJson.getApiKey(), savedStore.getApiKey());
        assertEquals(savedStoreJson.getApiSecret(), savedStore.getApiSecret());
        assertEquals(savedStoreJson.getPhoneNumber(), savedStore.getPhoneNumber());
        User savedStoreAdmin = savedStore.getAdmin();
        assertEquals(admin.getEmail(), savedStoreAdmin.getEmail());
        assertEquals(savedStoreJson.getPickupPoints().size(), savedStore.getPickupPoints().size());
    }
}
