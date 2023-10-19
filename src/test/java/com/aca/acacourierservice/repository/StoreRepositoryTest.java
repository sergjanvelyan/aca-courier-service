package com.aca.acacourierservice.repository;

import
        com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class StoreRepositoryTest {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PickupPointRepository pickupPointRepository;

    @AfterEach
    public void destroy() {
        storeRepository.deleteAll();
        userRepository.deleteAll();
        pickupPointRepository.deleteAll();
    }

    @Test
    public void testFindByApiKey() {
        User storeAdmin = new User();
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin12345");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        storeAdmin.setId(userRepository.save(storeAdmin).getId());

        PickupPoint pickupPoint = new PickupPoint();
        pickupPoint.setCity("Yerevan");
        pickupPoint.setZipCode("5004");
        pickupPoint.setCountry("Armenia");
        pickupPoint.setAddress("Address 12");
        pickupPoint.setPhoneNumber("+374896523");
        pickupPoint.setId(pickupPointRepository.save(pickupPoint).getId());

        List<PickupPoint> pickupPoints = new ArrayList<>();
        pickupPoints.add(pickupPoint);

        Store store = new Store();
        store.setName("First store");
        store.setStoreUrl("FirstStore.com");
        store.setApiKey("apiKey1");
        store.setApiSecret("apiSecret1");
        store.setPhoneNumber("+37477568923");
        store.setAdmin(storeAdmin);
        store.setPickupPoints(pickupPoints);
        store.setId(storeRepository.save(store).getId());

        Optional<Store> storeOptional = storeRepository.findByApiKey(store.getApiKey());
        assertTrue(storeOptional.isPresent());
        assertEquals(store,storeOptional.get());
    }
    @Test
    public void testFindByInvalidApiKey() {
        Optional<Store> storeOptional = storeRepository.findByApiKey("Invalid apiKey");
        assertFalse(storeOptional.isPresent());
    }
    @Test
    public void testFindByAdmin_Email() {
        User storeAdmin = new User();
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin12345");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        storeAdmin.setId(userRepository.save(storeAdmin).getId());

        PickupPoint pickupPoint = new PickupPoint();
        pickupPoint.setCity("Yerevan");
        pickupPoint.setZipCode("5004");
        pickupPoint.setCountry("Armenia");
        pickupPoint.setAddress("Address 12");
        pickupPoint.setPhoneNumber("+374896523");
        pickupPoint.setId(pickupPointRepository.save(pickupPoint).getId());

        List<PickupPoint> pickupPoints = new ArrayList<>();
        pickupPoints.add(pickupPoint);

        Store store = new Store();
        store.setName("First store");
        store.setStoreUrl("FirstStore.com");
        store.setApiKey("apiKey1");
        store.setApiSecret("apiSecret1");
        store.setPhoneNumber("+37477568923");
        store.setAdmin(storeAdmin);
        store.setPickupPoints(pickupPoints);
        store.setId(storeRepository.save(store).getId());

        Optional<Store> storeOptional = storeRepository.findByAdmin_Email(store.getAdmin().getEmail());
        assertTrue(storeOptional.isPresent());
        assertEquals(store,storeOptional.get());
    }
    @Test
    public void testFindByInvalidAdmin_Email() {
        Optional<Store> storeOptional = storeRepository.findByAdmin_Email("Invalid admin email");
        assertFalse(storeOptional.isPresent());
    }
}
