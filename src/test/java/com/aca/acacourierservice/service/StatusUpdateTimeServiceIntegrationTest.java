package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.StatusUpdateTimeConverter;
import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.entity.StatusUpdateTime;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.model.StatusUpdateTimeJson;
import com.aca.acacourierservice.repository.OrderRepository;
import com.aca.acacourierservice.repository.StatusUpdateTimeRepository;
import com.aca.acacourierservice.repository.StoreRepository;
import com.aca.acacourierservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class StatusUpdateTimeServiceIntegrationTest {
    @Autowired
    private StatusUpdateTimeService statusUpdateTimeService;
    @Autowired
    private StatusUpdateTimeRepository statusUpdateTimeRepository;
    @Autowired
    private StatusUpdateTimeConverter statusUpdateTimeConverter;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Test
    public void testAddStatusUpdateTime(){
        User storeAdmin = new User();
        storeAdmin.setId(1L);
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        userRepository.save(storeAdmin);

        Store store = new Store();
        store.setId(1L);
        store.setName("Store Name");
        store.setAdmin(storeAdmin);
        storeRepository.save(store);

        Order order = new Order();
        order.setId(1L);
        order.setOrderId("orderId1234");
        order.setStatus(Order.Status.SHIPPED);
        order.setStore(store);
        order.setCountry("Armenia");
        order.setCity("Yerevan");
        order.setAddress("Address 7");
        order.setPhone("+374-77-77-77-77");
        order.setZipCode(1005L);
        order.setFullName("FirstName LastName");
        order.setDeliveryPrice(10);
        order.setTotalPrice(50);
        order.setWeightKg(5.5);
        order.setSize(Order.Size.MEDIUM);
        order.setOrderConfirmedTime(LocalDateTime.of(2023, Month.AUGUST, 25, 15, 10, 3));
        orderRepository.save(order);

        StatusUpdateTimeJson statusUpdateTimeJson = new StatusUpdateTimeJson();
        statusUpdateTimeJson.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST,29,13,10,10));
        statusUpdateTimeJson.setUpdatedFrom(Order.Status.SHIPPED);
        statusUpdateTimeJson.setUpdatedTo(Order.Status.CANCELLED);
        statusUpdateTimeJson.setOrderId(order.getId());
        long id = statusUpdateTimeService.addStatusUpdateTime(statusUpdateTimeJson);
        StatusUpdateTime savedStatusUpdateTime= statusUpdateTimeRepository.findById(id).orElse(null);
        assertNotNull(savedStatusUpdateTime);
        assertEquals(statusUpdateTimeJson.getOrderId(),savedStatusUpdateTime.getOrder().getId());
        assertEquals(statusUpdateTimeJson.getUpdateTime(),savedStatusUpdateTime.getUpdateTime());
        assertEquals(statusUpdateTimeJson.getUpdatedFrom(),savedStatusUpdateTime.getUpdatedFrom());
        assertEquals(statusUpdateTimeJson.getUpdatedTo(),savedStatusUpdateTime.getUpdatedTo());

        StatusUpdateTimeJson savedStatusUpdateTimeJson = statusUpdateTimeConverter.convertToModel(savedStatusUpdateTime);
        assertEquals(statusUpdateTimeJson.getOrderId(),savedStatusUpdateTimeJson.getOrderId());
        assertEquals(statusUpdateTimeJson.getUpdateTime(),savedStatusUpdateTimeJson.getUpdateTime());
        assertEquals(statusUpdateTimeJson.getUpdatedFrom(),savedStatusUpdateTimeJson.getUpdatedFrom());
        assertEquals(statusUpdateTimeJson.getUpdatedTo(),savedStatusUpdateTimeJson.getUpdatedTo());
    }
}