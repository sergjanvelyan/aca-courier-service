package com.aca.acacourierservice.repository;

import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class PickupPointRepositoryTest {
    @Autowired
    private PickupPointRepository pickupPointRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreRepository storeRepository;
    @AfterEach
    public void destroy(){
        pickupPointRepository.deleteAll();
        storeRepository.deleteAll();
        userRepository.deleteAll();
    }
    @Test
    void testFindAllByStoreId() {
        User storeAdmin = new User();
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin12345");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        storeAdmin.setId(userRepository.save(storeAdmin).getId());

        Store store = new Store();
        store.setName("storeName");
        store.setAdmin(storeAdmin);
        store.setId(storeRepository.save(store).getId());

        PickupPoint pickupPoint = new PickupPoint();
        pickupPoint.setCity("Yerevan");
        pickupPoint.setCountry("Armenia");
        pickupPoint.setAddress("Halabyan 16");
        pickupPoint.setPhoneNumber("+37460589648");
        pickupPoint.setZipCode("0046");
        pickupPoint.setStore(store);
        pickupPoint.setId(pickupPointRepository.save(pickupPoint).getId());

        PickupPoint pickupPoint2 = new PickupPoint();
        pickupPoint2.setCity("Ijevan");
        pickupPoint2.setCountry("Armenia");
        pickupPoint2.setAddress("Ankaxutyan 8");
        pickupPoint2.setPhoneNumber("+37494586321");
        pickupPoint2.setZipCode("4001");
        pickupPoint2.setStore(store);
        pickupPoint2.setId(pickupPointRepository.save(pickupPoint2).getId());

        Page<PickupPoint> pickupPoints = pickupPointRepository.findAllByStoreId(store.getId(), PageRequest.of(0,5));
        assertEquals(2,pickupPoints.getNumberOfElements());
        List<PickupPoint> pickupPointList = pickupPoints.getContent();
        PickupPoint savedPickupPoint = pickupPointList.get(0);
        assertEquals(pickupPoint,savedPickupPoint);
        PickupPoint savedPickupPoint2 = pickupPointList.get(1);
        assertEquals(pickupPoint2,savedPickupPoint2);
    }
    @Test
    void testFindAllByInvalidStoreId() {
        Page<PickupPoint> pickupPoints = pickupPointRepository.findAllByStoreId(-1, PageRequest.of(0,5));
        assertEquals(0,pickupPoints.getNumberOfElements());
    }
    @Test
    void testExistsByIdAndStore_Admin_Email() {
        User storeAdmin = new User();
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin12345");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        storeAdmin.setId(userRepository.save(storeAdmin).getId());

        Store store = new Store();
        store.setName("storeName");
        store.setAdmin(storeAdmin);
        store.setId(storeRepository.save(store).getId());

        PickupPoint pickupPoint = new PickupPoint();
        pickupPoint.setCity("Yerevan");
        pickupPoint.setCountry("Armenia");
        pickupPoint.setAddress("Halabyan 16");
        pickupPoint.setPhoneNumber("+37460589648");
        pickupPoint.setZipCode("0046");
        pickupPoint.setStore(store);
        pickupPoint.setId(pickupPointRepository.save(pickupPoint).getId());

        boolean exists = pickupPointRepository.existsByIdAndStore_Admin_Email(pickupPoint.getId(),store.getAdmin().getEmail());
        assertTrue(exists);
    }
    @Test
    void testExistsByInvalidIdAndStore_Admin_Email() {
        User storeAdmin = new User();
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin12345");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        storeAdmin.setId(userRepository.save(storeAdmin).getId());

        Store store = new Store();
        store.setName("storeName");
        store.setAdmin(storeAdmin);
        store.setId(storeRepository.save(store).getId());

        PickupPoint pickupPoint = new PickupPoint();
        pickupPoint.setCity("Yerevan");
        pickupPoint.setCountry("Armenia");
        pickupPoint.setAddress("Halabyan 16");
        pickupPoint.setPhoneNumber("+37460589648");
        pickupPoint.setZipCode("0046");
        pickupPoint.setStore(store);
        pickupPoint.setId(pickupPointRepository.save(pickupPoint).getId());
        boolean exists = pickupPointRepository.existsByIdAndStore_Admin_Email(pickupPoint.getId(),"invalidStoreAdmin@gmail.com");
        assertFalse(exists);
        boolean exists1 = pickupPointRepository.existsByIdAndStore_Admin_Email(0,store.getAdmin().getEmail());
        assertFalse(exists1);
    }
    @Test
    void testFindByIdAndStore_Admin_Email() {
        User storeAdmin = new User();
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin12345");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        storeAdmin.setId(userRepository.save(storeAdmin).getId());

        Store store = new Store();
        store.setName("storeName");
        store.setAdmin(storeAdmin);
        store.setId(storeRepository.save(store).getId());

        PickupPoint pickupPoint = new PickupPoint();
        pickupPoint.setCity("Yerevan");
        pickupPoint.setCountry("Armenia");
        pickupPoint.setAddress("Halabyan 16");
        pickupPoint.setPhoneNumber("+37460589648");
        pickupPoint.setZipCode("0046");
        pickupPoint.setStore(store);
        pickupPoint.setId(pickupPointRepository.save(pickupPoint).getId());

        Optional<PickupPoint> pickupPointOptional = pickupPointRepository.findByIdAndStore_Admin_Email(pickupPoint.getId(),store.getAdmin().getEmail());
        assertTrue(pickupPointOptional.isPresent());
        PickupPoint savedPickupPoint = pickupPointOptional.get();
        assertEquals(pickupPoint,savedPickupPoint);
    }
    @Test
    void testFindByInvalidIdAndStore_Admin_Email() {
        User storeAdmin = new User();
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin12345");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        storeAdmin.setId(userRepository.save(storeAdmin).getId());

        Store store = new Store();
        store.setName("storeName");
        store.setAdmin(storeAdmin);
        store.setId(storeRepository.save(store).getId());

        PickupPoint pickupPoint = new PickupPoint();
        pickupPoint.setCity("Yerevan");
        pickupPoint.setCountry("Armenia");
        pickupPoint.setAddress("Halabyan 16");
        pickupPoint.setPhoneNumber("+37460589648");
        pickupPoint.setZipCode("0046");
        pickupPoint.setStore(store);
        pickupPoint.setId(pickupPointRepository.save(pickupPoint).getId());

        Optional<PickupPoint> pickupPointOptional = pickupPointRepository.findByIdAndStore_Admin_Email(pickupPoint.getId(),"invalidStoreAdmin@gmail.com");
        assertTrue(pickupPointOptional.isEmpty());

        Optional<PickupPoint> pickupPointOptional1 = pickupPointRepository.findByIdAndStore_Admin_Email(0,store.getAdmin().getEmail());
        assertTrue(pickupPointOptional1.isEmpty());
    }
}