package com.aca.acacourierservice.repository;

import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    public void testFindAllByPage() {
        User firstAdmin = new User();
        firstAdmin.setEmail("some@email.com");
        firstAdmin.setPassword("password123$");
        firstAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        userRepository.save(firstAdmin);

        User secondAdmin = new User();
        secondAdmin.setEmail("some2@email.com");
        secondAdmin.setPassword("password2123$");
        secondAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        userRepository.save(secondAdmin);

        PickupPoint pickupPoint = new PickupPoint();
        pickupPoint.setCity("city");
        pickupPoint.setZipCode(5004L);
        pickupPoint.setCountry("country");
        pickupPoint.setAddress("some address");
        pickupPoint.setPhoneNumber("+123456789");
        pickupPointRepository.save(pickupPoint);

        List<PickupPoint> pickupPoints = new ArrayList<>();
        pickupPoints.add(pickupPoint);

        Store firstStore = new Store();
        firstStore.setName("test store");
        firstStore.setStoreUrl("test.com");
        firstStore.setApiKey("apikey1");
        firstStore.setApiSecret("apisecret1");
        firstStore.setPhoneNumber("+123456789");
        firstStore.setAdmin(firstAdmin);
        firstStore.setPickupPoints(pickupPoints);
        storeRepository.save(firstStore);

        Store secondStore = new Store();
        secondStore.setName("test store");
        secondStore.setStoreUrl("test.com");
        secondStore.setApiKey("apikey2");
        secondStore.setApiSecret("apisecret2");
        secondStore.setPhoneNumber("+123456789");
        secondStore.setAdmin(secondAdmin);
        secondStore.setPickupPoints(pickupPoints);
        storeRepository.save(secondStore);

        Page<Store> storePage = storeRepository.findAll(PageRequest.of(0, 2));

        assertEquals(storePage.getTotalElements(), 2);
        List<Store> stores = storePage.toList();
        assertEquals(stores.get(0), firstStore);
        assertEquals(stores.get(1), secondStore);
    }
}
