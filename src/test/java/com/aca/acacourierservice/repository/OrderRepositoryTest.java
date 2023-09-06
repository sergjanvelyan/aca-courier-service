package com.aca.acacourierservice.repository;

import com.aca.acacourierservice.entity.Order;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreRepository storeRepository;
    @AfterEach
    public void destroy(){
        orderRepository.deleteAll();
        storeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testFindAllByStoreId() {
        User storeAdmin = new User();
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        storeAdmin.setId(userRepository.save(storeAdmin).getId());

        User courier = new User();
        courier.setEmail("courier@gmail.com");
        courier.setPassword("courier");
        courier.setRole(User.Role.ROLE_COURIER);
        courier.setId(userRepository.save(courier).getId());

        Store store = new Store();
        store.setName("Store Name");
        store.setAdmin(storeAdmin);
        store.setId(storeRepository.save(store).getId());

        Order order = new Order();
        order.setOrderId("orderId1234");
        order.setStatus(Order.Status.SHIPPED);
        order.setStore(store);
        order.setCourier(courier);
        order.setCountry("Armenia");
        order.setCity("Yerevan");
        order.setAddress("Address 7");
        order.setPhone("+374-77-77-77-77");
        order.setZipCode("0015");
        order.setFullName("FirstName LastName");
        order.setTrackingNumber("I entered this");
        order.setDeliveryPrice(10);
        order.setTotalPrice(50);
        order.setWeightKg(5.5);
        order.setSize(Order.Size.MEDIUM);
        order.setOrderConfirmedTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        order.setId(orderRepository.save(order).getId());

        Order anotherOrder = new Order();
        anotherOrder.setOrderId("orderId1235");
        anotherOrder.setStatus(Order.Status.NEW);
        anotherOrder.setStore(store);
        anotherOrder.setCourier(courier);
        anotherOrder.setCountry("Armenia");
        anotherOrder.setCity("Yerevan");
        anotherOrder.setAddress("Address 14");
        anotherOrder.setPhone("+374-99-99-99-99");
        anotherOrder.setZipCode("0027");
        anotherOrder.setFullName("FirstName LastName");
        anotherOrder.setTrackingNumber("I entered this");
        anotherOrder.setDeliveryPrice(12);
        anotherOrder.setTotalPrice(55);
        anotherOrder.setWeightKg(5.8);
        anotherOrder.setSize(Order.Size.LARGE);
        anotherOrder.setOrderConfirmedTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        anotherOrder.setId(orderRepository.save(anotherOrder).getId());

        Page<Order> ordersPage = orderRepository.findAllByStoreId(store.getId(), PageRequest.of(0,5));
        assertEquals(2,ordersPage.getTotalElements());
        List<Order> orders = ordersPage.getContent();
        assertEquals(order.getId(),orders.get(0).getId());
        assertEquals(order.getOrderId(),orders.get(0).getOrderId());
        assertEquals(order.getStatus(),orders.get(0).getStatus());
        assertEquals(order.getStore().getId(),orders.get(0).getStore().getId());
        assertEquals(order.getCourier().getId(),orders.get(0).getCourier().getId());
        assertEquals(order.getCountry(),orders.get(0).getCountry());
        assertEquals(order.getCity(),orders.get(0).getCity());
        assertEquals(order.getAddress(),orders.get(0).getAddress());
        assertEquals(order.getPhone(),orders.get(0).getPhone());
        assertEquals(order.getZipCode(),orders.get(0).getZipCode());
        assertEquals(order.getFullName(),orders.get(0).getFullName());
        assertEquals(order.getTrackingNumber(),orders.get(0).getTrackingNumber());
        assertEquals(order.getDeliveryPrice(),orders.get(0).getDeliveryPrice());
        assertEquals(order.getTotalPrice(),orders.get(0).getTotalPrice());
        assertEquals(order.getWeightKg(),orders.get(0).getWeightKg());
        assertEquals(order.getSize(),orders.get(0).getSize());
        assertEquals(order.getOrderConfirmedTime(),orders.get(0).getOrderConfirmedTime());
        assertEquals(order.getOrderDeliveredTime(),orders.get(0).getOrderDeliveredTime());

        assertEquals(anotherOrder.getId(),orders.get(1).getId());
        assertEquals(anotherOrder.getOrderId(),orders.get(1).getOrderId());
        assertEquals(anotherOrder.getStatus(),orders.get(1).getStatus());
        assertEquals(anotherOrder.getStore().getId(),orders.get(1).getStore().getId());
        assertEquals(anotherOrder.getCourier().getId(),orders.get(1).getCourier().getId());
        assertEquals(anotherOrder.getCountry(),orders.get(1).getCountry());
        assertEquals(anotherOrder.getCity(),orders.get(1).getCity());
        assertEquals(anotherOrder.getAddress(),orders.get(1).getAddress());
        assertEquals(anotherOrder.getPhone(),orders.get(1).getPhone());
        assertEquals(anotherOrder.getZipCode(),orders.get(1).getZipCode());
        assertEquals(anotherOrder.getFullName(),orders.get(1).getFullName());
        assertEquals(anotherOrder.getTrackingNumber(),orders.get(1).getTrackingNumber());
        assertEquals(anotherOrder.getDeliveryPrice(),orders.get(1).getDeliveryPrice());
        assertEquals(anotherOrder.getTotalPrice(),orders.get(1).getTotalPrice());
        assertEquals(anotherOrder.getWeightKg(),orders.get(1).getWeightKg());
        assertEquals(anotherOrder.getSize(),orders.get(1).getSize());
        assertEquals(anotherOrder.getOrderConfirmedTime(),orders.get(1).getOrderConfirmedTime());
        assertEquals(anotherOrder.getOrderDeliveredTime(),orders.get(1).getOrderDeliveredTime());

        Page<Order> ordersAnotherPage = orderRepository.findAllByStoreId(store.getId(), PageRequest.of(1,5));
        assertTrue(ordersAnotherPage.isEmpty());

        ordersAnotherPage = orderRepository.findAllByStoreId(store.getId(), PageRequest.of(0,1));
        assertEquals(1,ordersAnotherPage.getNumberOfElements());

        ordersAnotherPage = orderRepository.findAllByStoreId(store.getId(), PageRequest.of(1,1));
        assertEquals(1,ordersAnotherPage.getNumberOfElements());
    }
    @Test
    void testFindAllByInvalidStoreId(){
        Page<Order> ordersPage = orderRepository.findAllByStoreId(1, PageRequest.of(0,5));
        assertTrue(ordersPage.isEmpty());
    }
    @Test
    void testFindAllByCourierIsNull() {
        User storeAdmin = new User();
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        storeAdmin.setId(userRepository.save(storeAdmin).getId());

        User courier = new User();
        courier.setEmail("courier@gmail.com");
        courier.setPassword("courier");
        courier.setRole(User.Role.ROLE_COURIER);
        courier.setId(userRepository.save(courier).getId());

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
        order.setPhone("+374-77-77-77-77");
        order.setZipCode("0015");
        order.setFullName("FirstName LastName");
        order.setTrackingNumber("I entered this");
        order.setDeliveryPrice(10);
        order.setTotalPrice(50);
        order.setWeightKg(5.5);
        order.setSize(Order.Size.MEDIUM);
        order.setOrderConfirmedTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        order.setId(orderRepository.save(order).getId());

        Order orderTwo = new Order();
        orderTwo.setId(2L);
        orderTwo.setOrderId("orderId1234");
        orderTwo.setStatus(Order.Status.SHIPPED);
        orderTwo.setStore(store);
        orderTwo.setCountry("Armenia");
        orderTwo.setCity("Yerevan");
        orderTwo.setAddress("Address 7");
        orderTwo.setPhone("+374-77-77-77-77");
        orderTwo.setZipCode("0015");
        orderTwo.setFullName("FirstName LastName");
        orderTwo.setTrackingNumber("I entered this");
        orderTwo.setDeliveryPrice(10);
        orderTwo.setTotalPrice(50);
        orderTwo.setWeightKg(5.5);
        orderTwo.setSize(Order.Size.MEDIUM);
        orderTwo.setOrderConfirmedTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        orderTwo.setId(orderRepository.save(orderTwo).getId());

        Order anotherOrder = new Order();
        anotherOrder.setOrderId("orderId1235");
        anotherOrder.setStatus(Order.Status.NEW);
        anotherOrder.setStore(store);
        anotherOrder.setCourier(courier);
        anotherOrder.setCountry("Armenia");
        anotherOrder.setCity("Yerevan");
        anotherOrder.setAddress("Address 14");
        anotherOrder.setPhone("+374-99-99-99-99");
        anotherOrder.setZipCode("0027");
        anotherOrder.setFullName("FirstName LastName");
        anotherOrder.setTrackingNumber("I entered this");
        anotherOrder.setDeliveryPrice(12);
        anotherOrder.setTotalPrice(55);
        anotherOrder.setWeightKg(5.8);
        anotherOrder.setSize(Order.Size.LARGE);
        anotherOrder.setOrderConfirmedTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        anotherOrder.setId(orderRepository.save(anotherOrder).getId());

        Page<Order> unassignedOrders = orderRepository.findAllByCourierIsNull(PageRequest.of(0,5));
        assertEquals(2,unassignedOrders.getTotalElements());

        List<Order> orders = unassignedOrders.getContent();
        assertEquals(order.getId(),orders.get(0).getId());
        assertEquals(order.getOrderId(),orders.get(0).getOrderId());
        assertEquals(order.getStatus(),orders.get(0).getStatus());
        assertEquals(order.getStore().getId(),orders.get(0).getStore().getId());
        assertNull(order.getCourier());
        assertEquals(order.getCountry(),orders.get(0).getCountry());
        assertEquals(order.getCity(),orders.get(0).getCity());
        assertEquals(order.getAddress(),orders.get(0).getAddress());
        assertEquals(order.getPhone(),orders.get(0).getPhone());
        assertEquals(order.getZipCode(),orders.get(0).getZipCode());
        assertEquals(order.getFullName(),orders.get(0).getFullName());
        assertEquals(order.getTrackingNumber(),orders.get(0).getTrackingNumber());
        assertEquals(order.getDeliveryPrice(),orders.get(0).getDeliveryPrice());
        assertEquals(order.getTotalPrice(),orders.get(0).getTotalPrice());
        assertEquals(order.getWeightKg(),orders.get(0).getWeightKg());
        assertEquals(order.getSize(),orders.get(0).getSize());
        assertEquals(order.getOrderConfirmedTime(),orders.get(0).getOrderConfirmedTime());

        assertEquals(orderTwo.getId(),orders.get(1).getId());
        assertEquals(orderTwo.getOrderId(),orders.get(1).getOrderId());
        assertEquals(orderTwo.getStatus(),orders.get(1).getStatus());
        assertEquals(orderTwo.getStore().getId(),orders.get(1).getStore().getId());
        assertNull(orderTwo.getCourier());
        assertEquals(orderTwo.getCountry(),orders.get(1).getCountry());
        assertEquals(orderTwo.getCity(),orders.get(1).getCity());
        assertEquals(orderTwo.getAddress(),orders.get(1).getAddress());
        assertEquals(orderTwo.getPhone(),orders.get(1).getPhone());
        assertEquals(orderTwo.getZipCode(),orders.get(1).getZipCode());
        assertEquals(orderTwo.getFullName(),orders.get(1).getFullName());
        assertEquals(orderTwo.getTrackingNumber(),orders.get(1).getTrackingNumber());
        assertEquals(orderTwo.getDeliveryPrice(),orders.get(1).getDeliveryPrice());
        assertEquals(orderTwo.getTotalPrice(),orders.get(1).getTotalPrice());
        assertEquals(orderTwo.getWeightKg(),orders.get(1).getWeightKg());
        assertEquals(orderTwo.getSize(),orders.get(1).getSize());
        assertEquals(orderTwo.getOrderConfirmedTime(),orders.get(1).getOrderConfirmedTime());
    }
    @Test
    void testFindAllByCourierIsNullWhenAllOrdersAssigned() {
        User storeAdmin = new User();
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        storeAdmin.setId(userRepository.save(storeAdmin).getId());

        User courier = new User();
        courier.setEmail("courier@gmail.com");
        courier.setPassword("courier");
        courier.setRole(User.Role.ROLE_COURIER);
        courier.setId(userRepository.save(courier).getId());

        Store store = new Store();
        store.setName("Store Name");
        store.setAdmin(storeAdmin);
        store.setId(storeRepository.save(store).getId());

        Order order = new Order();
        order.setOrderId("orderId1234");
        order.setStatus(Order.Status.SHIPPED);
        order.setStore(store);
        order.setCourier(courier);
        order.setCountry("Armenia");
        order.setCity("Yerevan");
        order.setAddress("Address 7");
        order.setPhone("+374-77-77-77-77");
        order.setZipCode("0015");
        order.setFullName("FirstName LastName");
        order.setTrackingNumber("I entered this");
        order.setDeliveryPrice(10);
        order.setTotalPrice(50);
        order.setWeightKg(5.5);
        order.setSize(Order.Size.MEDIUM);
        order.setOrderConfirmedTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        order.setId(orderRepository.save(order).getId());

        Order orderTwo = new Order();
        orderTwo.setOrderId("orderId1234");
        orderTwo.setStatus(Order.Status.SHIPPED);
        orderTwo.setStore(store);
        orderTwo.setCourier(courier);
        orderTwo.setCountry("Armenia");
        orderTwo.setCity("Yerevan");
        orderTwo.setAddress("Address 7");
        orderTwo.setPhone("+374-77-77-77-77");
        orderTwo.setZipCode("0015");
        orderTwo.setFullName("FirstName LastName");
        orderTwo.setTrackingNumber("I entered this");
        orderTwo.setDeliveryPrice(10);
        orderTwo.setTotalPrice(50);
        orderTwo.setWeightKg(5.5);
        orderTwo.setSize(Order.Size.MEDIUM);
        orderTwo.setOrderConfirmedTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        orderTwo.setId(orderRepository.save(orderTwo).getId());

        Order anotherOrder = new Order();
        anotherOrder.setOrderId("orderId1235");
        anotherOrder.setStatus(Order.Status.NEW);
        anotherOrder.setStore(store);
        anotherOrder.setCourier(courier);
        anotherOrder.setCountry("Armenia");
        anotherOrder.setCity("Yerevan");
        anotherOrder.setAddress("Address 14");
        anotherOrder.setPhone("+374-99-99-99-99");
        anotherOrder.setZipCode("0027");
        anotherOrder.setFullName("FirstName LastName");
        anotherOrder.setTrackingNumber("I entered this");
        anotherOrder.setDeliveryPrice(12);
        anotherOrder.setTotalPrice(55);
        anotherOrder.setWeightKg(5.8);
        anotherOrder.setSize(Order.Size.LARGE);
        anotherOrder.setOrderConfirmedTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        anotherOrder.setId(orderRepository.save(anotherOrder).getId());

        Page<Order> unassignedOrders = orderRepository.findAllByCourierIsNull(PageRequest.of(0,5));
        assertTrue(unassignedOrders.isEmpty());
    }
    @Test
    void testFindAllByCourierId() {
        User storeAdmin = new User();
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        storeAdmin.setId(userRepository.save(storeAdmin).getId());

        User courier = new User();
        courier.setEmail("courier@gmail.com");
        courier.setPassword("courier");
        courier.setRole(User.Role.ROLE_COURIER);
        courier.setId(userRepository.save(courier).getId());

        User anotherCourier = new User();
        anotherCourier.setEmail("anotherCourier@gmail.com");
        anotherCourier.setPassword("anotherCourier");
        anotherCourier.setRole(User.Role.ROLE_COURIER);
        anotherCourier.setId(userRepository.save(anotherCourier).getId());

        Store store = new Store();
        store.setName("Store Name");
        store.setAdmin(storeAdmin);
        store.setId(storeRepository.save(store).getId());

        Order order = new Order();
        order.setOrderId("orderId1234");
        order.setStatus(Order.Status.SHIPPED);
        order.setStore(store);
        order.setCourier(courier);
        order.setCountry("Armenia");
        order.setCity("Yerevan");
        order.setAddress("Address 7");
        order.setPhone("+374-77-77-77-77");
        order.setZipCode("0015");
        order.setFullName("FirstName LastName");
        order.setTrackingNumber("I entered this");
        order.setDeliveryPrice(10);
        order.setTotalPrice(50);
        order.setWeightKg(5.5);
        order.setSize(Order.Size.MEDIUM);
        order.setOrderConfirmedTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        order.setId(orderRepository.save(order).getId());

        Order orderTwo = new Order();
        orderTwo.setOrderId("orderId1234");
        orderTwo.setStatus(Order.Status.SHIPPED);
        orderTwo.setStore(store);
        orderTwo.setCourier(courier);
        orderTwo.setCountry("Armenia");
        orderTwo.setCity("Yerevan");
        orderTwo.setAddress("Address 7");
        orderTwo.setPhone("+374-77-77-77-77");
        orderTwo.setZipCode("0015");
        orderTwo.setFullName("FirstName LastName");
        orderTwo.setTrackingNumber("I entered this");
        orderTwo.setDeliveryPrice(10);
        orderTwo.setTotalPrice(50);
        orderTwo.setWeightKg(5.5);
        orderTwo.setSize(Order.Size.MEDIUM);
        orderTwo.setOrderConfirmedTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        orderTwo.setId(orderRepository.save(orderTwo).getId());

        Order anotherOrder = new Order();
        anotherOrder.setOrderId("orderId1235");
        anotherOrder.setStatus(Order.Status.NEW);
        anotherOrder.setStore(store);
        anotherOrder.setCourier(anotherCourier);
        anotherOrder.setCountry("Armenia");
        anotherOrder.setCity("Yerevan");
        anotherOrder.setAddress("Address 14");
        anotherOrder.setPhone("+374-99-99-99-99");
        anotherOrder.setZipCode("0027");
        anotherOrder.setFullName("FirstName LastName");
        anotherOrder.setTrackingNumber("I entered this");
        anotherOrder.setDeliveryPrice(12);
        anotherOrder.setTotalPrice(55);
        anotherOrder.setWeightKg(5.8);
        anotherOrder.setSize(Order.Size.LARGE);
        anotherOrder.setOrderConfirmedTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        anotherOrder.setId(orderRepository.save(anotherOrder).getId());

        Page<Order> ordersPage = orderRepository.findAllByCourierId(courier.getId(),PageRequest.of(0,5));
        assertEquals(2,ordersPage.getTotalElements());

        List<Order> courierOrders = ordersPage.getContent();

        assertEquals(order.getId(),courierOrders.get(0).getId());
        assertEquals(order.getOrderId(),courierOrders.get(0).getOrderId());
        assertEquals(order.getStatus(),courierOrders.get(0).getStatus());
        assertEquals(order.getStore().getId(),courierOrders.get(0).getStore().getId());
        assertEquals(order.getCourier().getId(),courierOrders.get(0).getCourier().getId());
        assertEquals(order.getCountry(),courierOrders.get(0).getCountry());
        assertEquals(order.getCity(),courierOrders.get(0).getCity());
        assertEquals(order.getAddress(),courierOrders.get(0).getAddress());
        assertEquals(order.getPhone(),courierOrders.get(0).getPhone());
        assertEquals(order.getZipCode(),courierOrders.get(0).getZipCode());
        assertEquals(order.getFullName(),courierOrders.get(0).getFullName());
        assertEquals(order.getTrackingNumber(),courierOrders.get(0).getTrackingNumber());
        assertEquals(order.getDeliveryPrice(),courierOrders.get(0).getDeliveryPrice());
        assertEquals(order.getTotalPrice(),courierOrders.get(0).getTotalPrice());
        assertEquals(order.getWeightKg(),courierOrders.get(0).getWeightKg());
        assertEquals(order.getSize(),courierOrders.get(0).getSize());
        assertEquals(order.getOrderConfirmedTime(),courierOrders.get(0).getOrderConfirmedTime());

        assertEquals(orderTwo.getId(),courierOrders.get(1).getId());
        assertEquals(orderTwo.getOrderId(),courierOrders.get(1).getOrderId());
        assertEquals(orderTwo.getStatus(),courierOrders.get(1).getStatus());
        assertEquals(orderTwo.getStore().getId(),courierOrders.get(1).getStore().getId());
        assertEquals(order.getCourier().getId(),courierOrders.get(1).getCourier().getId());
        assertEquals(orderTwo.getCountry(),courierOrders.get(1).getCountry());
        assertEquals(orderTwo.getCity(),courierOrders.get(1).getCity());
        assertEquals(orderTwo.getAddress(),courierOrders.get(1).getAddress());
        assertEquals(orderTwo.getPhone(),courierOrders.get(1).getPhone());
        assertEquals(orderTwo.getZipCode(),courierOrders.get(1).getZipCode());
        assertEquals(orderTwo.getFullName(),courierOrders.get(1).getFullName());
        assertEquals(orderTwo.getTrackingNumber(),courierOrders.get(1).getTrackingNumber());
        assertEquals(orderTwo.getDeliveryPrice(),courierOrders.get(1).getDeliveryPrice());
        assertEquals(orderTwo.getTotalPrice(),courierOrders.get(1).getTotalPrice());
        assertEquals(orderTwo.getWeightKg(),courierOrders.get(1).getWeightKg());
        assertEquals(orderTwo.getSize(),courierOrders.get(1).getSize());
        assertEquals(orderTwo.getOrderConfirmedTime(),courierOrders.get(1).getOrderConfirmedTime());

        Page<Order> anotherOrdersPage = orderRepository.findAllByCourierId(anotherCourier.getId(),PageRequest.of(0,5));
        assertEquals(1,anotherOrdersPage.getTotalElements());
        List<Order> anotherCourierOrders = anotherOrdersPage.getContent();
        assertEquals(anotherOrder.getId(),anotherCourierOrders.get(0).getId());
        assertEquals(anotherOrder.getOrderId(),anotherCourierOrders.get(0).getOrderId());
        assertEquals(anotherOrder.getStatus(),anotherCourierOrders.get(0).getStatus());
        assertEquals(anotherOrder.getStore().getId(),anotherCourierOrders.get(0).getStore().getId());
        assertEquals(anotherOrder.getCourier().getId(),anotherCourierOrders.get(0).getCourier().getId());
        assertEquals(anotherOrder.getCountry(),anotherCourierOrders.get(0).getCountry());
        assertEquals(anotherOrder.getCity(),anotherCourierOrders.get(0).getCity());
        assertEquals(anotherOrder.getAddress(),anotherCourierOrders.get(0).getAddress());
        assertEquals(anotherOrder.getPhone(),anotherCourierOrders.get(0).getPhone());
        assertEquals(anotherOrder.getZipCode(),anotherCourierOrders.get(0).getZipCode());
        assertEquals(anotherOrder.getFullName(),anotherCourierOrders.get(0).getFullName());
        assertEquals(anotherOrder.getTrackingNumber(),anotherCourierOrders.get(0).getTrackingNumber());
        assertEquals(anotherOrder.getDeliveryPrice(),anotherCourierOrders.get(0).getDeliveryPrice());
        assertEquals(anotherOrder.getTotalPrice(),anotherCourierOrders.get(0).getTotalPrice());
        assertEquals(anotherOrder.getWeightKg(),anotherCourierOrders.get(0).getWeightKg());
        assertEquals(anotherOrder.getSize(),anotherCourierOrders.get(0).getSize());
        assertEquals(anotherOrder.getOrderConfirmedTime(),anotherCourierOrders.get(0).getOrderConfirmedTime());
        assertEquals(anotherOrder.getOrderDeliveredTime(),anotherCourierOrders.get(0).getOrderDeliveredTime());
    }
    @Test
    void testFindAllByInvalidCourierId() {
        User storeAdmin = new User();
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        storeAdmin.setId(userRepository.save(storeAdmin).getId());

        User courier = new User();
        courier.setEmail("courier@gmail.com");
        courier.setPassword("courier");
        courier.setRole(User.Role.ROLE_COURIER);
        courier.setId(userRepository.save(courier).getId());

        Store store = new Store();
        store.setName("Store Name");
        store.setAdmin(storeAdmin);
        store.setId(storeRepository.save(store).getId());

        Order order = new Order();
        order.setOrderId("orderId1234");
        order.setStatus(Order.Status.SHIPPED);
        order.setStore(store);
        order.setCourier(courier);
        order.setCountry("Armenia");
        order.setCity("Yerevan");
        order.setAddress("Address 7");
        order.setPhone("+374-77-77-77-77");
        order.setZipCode("0015");
        order.setFullName("FirstName LastName");
        order.setTrackingNumber("I entered this");
        order.setDeliveryPrice(10);
        order.setTotalPrice(50);
        order.setWeightKg(5.5);
        order.setSize(Order.Size.MEDIUM);
        order.setOrderConfirmedTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        order.setId(orderRepository.save(order).getId());

        Order orderTwo = new Order();
        orderTwo.setOrderId("orderId1234");
        orderTwo.setStatus(Order.Status.SHIPPED);
        orderTwo.setStore(store);
        orderTwo.setCourier(courier);
        orderTwo.setCountry("Armenia");
        orderTwo.setCity("Yerevan");
        orderTwo.setAddress("Address 7");
        orderTwo.setPhone("+374-77-77-77-77");
        orderTwo.setZipCode("0015");
        orderTwo.setFullName("FirstName LastName");
        orderTwo.setTrackingNumber("I entered this");
        orderTwo.setDeliveryPrice(10);
        orderTwo.setTotalPrice(50);
        orderTwo.setWeightKg(5.5);
        orderTwo.setSize(Order.Size.MEDIUM);
        orderTwo.setOrderConfirmedTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        orderTwo.setId(orderRepository.save(orderTwo).getId());

        Page<Order> ordersPage = orderRepository.findAllByCourierId(courier.getId()+1,PageRequest.of(0,5));
        assertTrue(ordersPage.isEmpty());
    }
}