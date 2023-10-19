package com.aca.acacourierservice.repository;

import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.entity.StatusUpdateTime;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class StatusUpdateTimeRepositoryTest {
    @Autowired
    private StatusUpdateTimeRepository statusUpdateTimeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private OrderRepository orderRepository;
    @AfterEach
    public void destroy(){
        statusUpdateTimeRepository.deleteAll();
        orderRepository.deleteAll();
        storeRepository.deleteAll();
        userRepository.deleteAll();
    }
    @Test
    public void testFindAllByOrderId(){
        User storeAdmin = new User();
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin12345");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        storeAdmin.setId(userRepository.save(storeAdmin).getId());

        Store store = new Store();
        store.setName("Store Name");
        store.setAdmin(storeAdmin);
        store.setId(storeRepository.save(store).getId());

        Order order = new Order();
        order.setOrderId("orderId1234");
        order.setStatus(Order.Status.SHIPPED);
        order.setStore(store);
        order.setCountry("Armenia");
        order.setCity("Yerevan");
        order.setAddress("Address 7");
        order.setPhone("+37477777777");
        order.setZipCode("0015");
        order.setFullName("FirstName LastName");
        order.setDeliveryPrice(10.0);
        order.setTotalPrice(50.0);
        order.setWeightKg(5.5);
        order.setSize(Order.Size.MEDIUM);
        order.setOrderConfirmedTime(LocalDateTime.of(2023, Month.AUGUST, 25, 15, 10, 3));
        order.setId(orderRepository.save(order).getId());

        StatusUpdateTime firstStatusUpdateTime = new StatusUpdateTime();
        firstStatusUpdateTime.setOrder(order);
        firstStatusUpdateTime.setUpdatedTo(Order.Status.DELIVERING);
        firstStatusUpdateTime.setUpdatedFrom(Order.Status.SHIPPED);
        firstStatusUpdateTime.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST, 28, 21, 23, 23));
        firstStatusUpdateTime.setId(statusUpdateTimeRepository.save(firstStatusUpdateTime).getId());

        StatusUpdateTime secondStatusUpdateTime = new StatusUpdateTime();
        secondStatusUpdateTime.setOrder(order);
        secondStatusUpdateTime.setUpdatedTo(Order.Status.DELIVERED);
        secondStatusUpdateTime.setUpdatedFrom(Order.Status.DELIVERING);
        secondStatusUpdateTime.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST,28,22,23,23));
        secondStatusUpdateTime.setAdditionalInfo("Additional info");
        secondStatusUpdateTime.setId(statusUpdateTimeRepository.save(secondStatusUpdateTime).getId());

        List<StatusUpdateTime> statusUpdateTimes = statusUpdateTimeRepository.findAllByOrderId(order.getId());
        assertEquals(2,statusUpdateTimes.size());

        StatusUpdateTime savedFirstStatusUpdateTime = statusUpdateTimes.get(0);
        assertEquals(firstStatusUpdateTime,savedFirstStatusUpdateTime);

        StatusUpdateTime savedSecondStatusUpdateTime = statusUpdateTimes.get(1);
        assertEquals(secondStatusUpdateTime,savedSecondStatusUpdateTime);
    }
    @Test
    public void testFindAllByInvalidOrderId(){
        List<StatusUpdateTime> statusUpdateTimes = statusUpdateTimeRepository.findAllByOrderId(0);
        Assertions.assertThat(statusUpdateTimes.size()).isEqualTo(0);
    }
    @Test
    public void testFindAllByOrder_TrackingNumber(){
        User storeAdmin = new User();
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin12345");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        storeAdmin.setId(userRepository.save(storeAdmin).getId());

        Store store = new Store();
        store.setName("Store Name");
        store.setAdmin(storeAdmin);
        store.setId(storeRepository.save(store).getId());

        Order order = new Order();
        order.setOrderId("orderId1234");
        order.setStatus(Order.Status.SHIPPED);
        order.setStore(store);
        order.setCountry("Armenia");
        order.setCity("Yerevan");
        order.setAddress("Address 7");
        order.setPhone("+37477777777");
        order.setZipCode("0015");
        order.setFullName("FirstName LastName");
        order.setDeliveryPrice(10.0);
        order.setTotalPrice(50.0);
        order.setWeightKg(5.5);
        order.setSize(Order.Size.MEDIUM);
        order.setOrderConfirmedTime(LocalDateTime.of(2023, Month.AUGUST, 25, 15, 10, 3));
        order.setId(orderRepository.save(order).getId());

        StatusUpdateTime firstStatusUpdateTime = new StatusUpdateTime();
        firstStatusUpdateTime.setOrder(order);
        firstStatusUpdateTime.setUpdatedTo(Order.Status.DELIVERING);
        firstStatusUpdateTime.setUpdatedFrom(Order.Status.SHIPPED);
        firstStatusUpdateTime.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST, 28, 21, 23, 23));
        firstStatusUpdateTime.setId(statusUpdateTimeRepository.save(firstStatusUpdateTime).getId());

        StatusUpdateTime secondStatusUpdateTime = new StatusUpdateTime();
        secondStatusUpdateTime.setOrder(order);
        secondStatusUpdateTime.setUpdatedTo(Order.Status.DELIVERED);
        secondStatusUpdateTime.setUpdatedFrom(Order.Status.DELIVERING);
        secondStatusUpdateTime.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST,28,22,23,23));
        secondStatusUpdateTime.setAdditionalInfo("Ok");
        secondStatusUpdateTime.setId(statusUpdateTimeRepository.save(secondStatusUpdateTime).getId());

        List<StatusUpdateTime> statusUpdateTimes = statusUpdateTimeRepository.findAllByOrder_TrackingNumber(order.getTrackingNumber());
        assertEquals(2,statusUpdateTimes.size());

        StatusUpdateTime savedFirstStatusUpdateTime = statusUpdateTimes.get(0);
        assertEquals(firstStatusUpdateTime,savedFirstStatusUpdateTime);

        StatusUpdateTime savedSecondStatusUpdateTime = statusUpdateTimes.get(1);
        assertEquals(secondStatusUpdateTime,savedSecondStatusUpdateTime);
    }
    @Test
    public void testFindAllByInvalidOrder_TrackingNumber(){
        List<StatusUpdateTime> statusUpdateTimes = statusUpdateTimeRepository.findAllByOrder_TrackingNumber("Invalid tracking number");
        assertTrue(statusUpdateTimes.isEmpty());
    }
}