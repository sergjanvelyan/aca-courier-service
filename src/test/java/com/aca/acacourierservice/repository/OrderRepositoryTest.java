package com.aca.acacourierservice.repository;

import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
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
    void testFindAllByStore_Admin_Id() {
        User storeAdmin = new User();
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin12345");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        storeAdmin.setId(userRepository.save(storeAdmin).getId());

        User courier = new User();
        courier.setEmail("courier@gmail.com");
        courier.setPassword("courier12345");
        courier.setRole(User.Role.ROLE_COURIER);
        courier.setId(userRepository.save(courier).getId());

        Store store = new Store();
        store.setName("storeName");
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
        order.setPhone("+37477777777");
        order.setZipCode("0015");
        order.setFullName("FirstName LastName");
        order.setTrackingNumber("I entered this");
        order.setDeliveryPrice(10.0);
        order.setTotalPrice(50.0);
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
        anotherOrder.setPhone("+37499999999");
        anotherOrder.setZipCode("0027");
        anotherOrder.setFullName("FirstName LastName");
        anotherOrder.setTrackingNumber("I entered this");
        anotherOrder.setDeliveryPrice(12.0);
        anotherOrder.setTotalPrice(55.0);
        anotherOrder.setWeightKg(5.8);
        anotherOrder.setSize(Order.Size.LARGE);
        anotherOrder.setOrderConfirmedTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        anotherOrder.setId(orderRepository.save(anotherOrder).getId());

        Page<Order> ordersPage = orderRepository.findAllByStore_Admin_Id(storeAdmin.getId(), PageRequest.of(0,5));
        assertEquals(2,ordersPage.getTotalElements());
        List<Order> orders = ordersPage.getContent();

        Order savedOrder = orders.get(0);
        assertEquals(order,savedOrder);

        Order savedAnotherOrder = orders.get(1);
        assertEquals(anotherOrder,savedAnotherOrder);

        Page<Order> ordersAnotherPage = orderRepository.findAllByStore_Admin_Id(storeAdmin.getId(), PageRequest.of(1,5));
        assertTrue(ordersAnotherPage.isEmpty());

        ordersAnotherPage = orderRepository.findAllByStore_Admin_Id(storeAdmin.getId(), PageRequest.of(0,1));
        assertEquals(1,ordersAnotherPage.getNumberOfElements());

        ordersAnotherPage = orderRepository.findAllByStore_Admin_Id(storeAdmin.getId(), PageRequest.of(1,1));
        assertEquals(1,ordersAnotherPage.getNumberOfElements());
    }
    @Test
    void testFindAllByInvalidStoreId(){
        Page<Order> ordersPage = orderRepository.findAllByStore_Admin_Id(1, PageRequest.of(0,5));
        assertTrue(ordersPage.isEmpty());
        Page<Order> anotherOrdersPage = orderRepository.findAllByStore_Admin_Id(-1, PageRequest.of(0,5));
        assertTrue(anotherOrdersPage.isEmpty());
    }
    @Test
    void testFindAllByCourierIsNull() {
        User storeAdmin = new User();
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin12345");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        storeAdmin.setId(userRepository.save(storeAdmin).getId());

        User courier = new User();
        courier.setEmail("courier@gmail.com");
        courier.setPassword("courier12345");
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
        order.setPhone("+37477777777");
        order.setZipCode("0015");
        order.setFullName("FirstName LastName");
        order.setTrackingNumber("I entered this");
        order.setDeliveryPrice(10.0);
        order.setTotalPrice(50.0);
        order.setWeightKg(5.5);
        order.setSize(Order.Size.MEDIUM);
        order.setOrderConfirmedTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        order.setId(orderRepository.save(order).getId());

        Order orderTwo = new Order();
        orderTwo.setOrderId("orderId1234");
        orderTwo.setStatus(Order.Status.SHIPPED);
        orderTwo.setStore(store);
        orderTwo.setCountry("Armenia");
        orderTwo.setCity("Yerevan");
        orderTwo.setAddress("Address 7");
        orderTwo.setPhone("+37477777777");
        orderTwo.setZipCode("0015");
        orderTwo.setFullName("FirstName LastName");
        orderTwo.setTrackingNumber("I entered this");
        orderTwo.setDeliveryPrice(10.0);
        orderTwo.setTotalPrice(50.0);
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
        anotherOrder.setPhone("+37499999999");
        anotherOrder.setZipCode("0027");
        anotherOrder.setFullName("FirstName LastName");
        anotherOrder.setTrackingNumber("I entered this");
        anotherOrder.setDeliveryPrice(12.0);
        anotherOrder.setTotalPrice(55.0);
        anotherOrder.setWeightKg(5.8);
        anotherOrder.setSize(Order.Size.LARGE);
        anotherOrder.setOrderConfirmedTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        anotherOrder.setId(orderRepository.save(anotherOrder).getId());

        Page<Order> unassignedOrders = orderRepository.findAllByCourierIsNull(PageRequest.of(0,5));
        assertEquals(2,unassignedOrders.getTotalElements());

        List<Order> orders = unassignedOrders.getContent();

        Order savedOrder = orders.get(0);
        assertEquals(order,savedOrder);
        assertNull(savedOrder.getCourier());

        Order savedOrderTwo = orders.get(1);
        assertEquals(orderTwo,savedOrderTwo);
        assertNull(savedOrderTwo.getCourier());
    }
    @Test
    void testFindAllByCourierIsNullWhenAllOrdersAssigned() {
        User storeAdmin = new User();
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin12345");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        storeAdmin.setId(userRepository.save(storeAdmin).getId());

        User courier = new User();
        courier.setEmail("courier@gmail.com");
        courier.setPassword("courier12345");
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
        order.setPhone("+37477777777");
        order.setZipCode("0015");
        order.setFullName("FirstName LastName");
        order.setTrackingNumber("I entered this");
        order.setDeliveryPrice(10.0);
        order.setTotalPrice(50.0);
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
        orderTwo.setPhone("+37477777777");
        orderTwo.setZipCode("0015");
        orderTwo.setFullName("FirstName LastName");
        orderTwo.setTrackingNumber("I entered this");
        orderTwo.setDeliveryPrice(10.0);
        orderTwo.setTotalPrice(50.0);
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
        anotherOrder.setPhone("+37499999999");
        anotherOrder.setZipCode("0027");
        anotherOrder.setFullName("FirstName LastName");
        anotherOrder.setTrackingNumber("I entered this");
        anotherOrder.setDeliveryPrice(12.0);
        anotherOrder.setTotalPrice(55.0);
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
        storeAdmin.setPassword("storeAdmin12345");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        storeAdmin.setId(userRepository.save(storeAdmin).getId());

        User courier = new User();
        courier.setEmail("courier@gmail.com");
        courier.setPassword("courier12345");
        courier.setRole(User.Role.ROLE_COURIER);
        courier.setId(userRepository.save(courier).getId());

        User anotherCourier = new User();
        anotherCourier.setEmail("anotherCourier@gmail.com");
        anotherCourier.setPassword("anotherCourier12345");
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
        order.setPhone("+37477777777");
        order.setZipCode("0015");
        order.setFullName("FirstName LastName");
        order.setTrackingNumber("I entered this");
        order.setDeliveryPrice(10.0);
        order.setTotalPrice(50.0);
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
        orderTwo.setPhone("+37477777777");
        orderTwo.setZipCode("0015");
        orderTwo.setFullName("FirstName LastName");
        orderTwo.setTrackingNumber("I entered this");
        orderTwo.setDeliveryPrice(10.0);
        orderTwo.setTotalPrice(50.0);
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
        anotherOrder.setPhone("+37499999999");
        anotherOrder.setZipCode("0027");
        anotherOrder.setFullName("FirstName LastName");
        anotherOrder.setTrackingNumber("I entered this");
        anotherOrder.setDeliveryPrice(12.0);
        anotherOrder.setTotalPrice(55.0);
        anotherOrder.setWeightKg(5.8);
        anotherOrder.setSize(Order.Size.LARGE);
        anotherOrder.setOrderConfirmedTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        anotherOrder.setId(orderRepository.save(anotherOrder).getId());

        Page<Order> ordersPage = orderRepository.findAllByCourierId(courier.getId(),PageRequest.of(0,5));
        assertEquals(2,ordersPage.getTotalElements());

        List<Order> courierOrders = ordersPage.getContent();

        Order savedOrder = courierOrders.get(0);
        assertEquals(order,savedOrder);

        Order savedOrderTwo = courierOrders.get(1);
        assertEquals(orderTwo,savedOrderTwo);

        Page<Order> anotherOrdersPage = orderRepository.findAllByCourierId(anotherCourier.getId(),PageRequest.of(0,5));
        assertEquals(1,anotherOrdersPage.getTotalElements());
        List<Order> anotherCourierOrders = anotherOrdersPage.getContent();

        Order savedAnotherOrder = anotherCourierOrders.get(0);
        assertEquals(anotherOrder,savedAnotherOrder);
    }
    @Test
    void testFindAllByInvalidCourierId() {
        User storeAdmin = new User();
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin12345");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        storeAdmin.setId(userRepository.save(storeAdmin).getId());

        User courier = new User();
        courier.setEmail("courier@gmail.com");
        courier.setPassword("courier12345");
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
        order.setPhone("+37477777777");
        order.setZipCode("0015");
        order.setFullName("FirstName LastName");
        order.setTrackingNumber("I entered this");
        order.setDeliveryPrice(10.0);
        order.setTotalPrice(50.0);
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
        orderTwo.setPhone("+37477777777");
        orderTwo.setZipCode("0015");
        orderTwo.setFullName("FirstName LastName");
        orderTwo.setTrackingNumber("I entered this");
        orderTwo.setDeliveryPrice(10.0);
        orderTwo.setTotalPrice(50.0);
        orderTwo.setWeightKg(5.5);
        orderTwo.setSize(Order.Size.MEDIUM);
        orderTwo.setOrderConfirmedTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        orderTwo.setId(orderRepository.save(orderTwo).getId());

        Page<Order> ordersPage = orderRepository.findAllByCourierId(courier.getId()+1,PageRequest.of(0,5));
        assertTrue(ordersPage.isEmpty());
    }
    @Test
    void testFindAllWithSpecification(){
        User storeAdmin = new User();
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin12345");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        storeAdmin.setId(userRepository.save(storeAdmin).getId());

        User courier = new User();
        courier.setEmail("courier@gmail.com");
        courier.setPassword("courier12345");
        courier.setRole(User.Role.ROLE_COURIER);
        courier.setId(userRepository.save(courier).getId());

        User anotherCourier = new User();
        anotherCourier.setEmail("anotherCourier@gmail.com");
        anotherCourier.setPassword("anotherCourier12345");
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
        order.setPhone("+37477777777");
        order.setZipCode("0015");
        order.setFullName("FirstName LastName");
        order.setTrackingNumber("I entered this");
        order.setDeliveryPrice(10.0);
        order.setTotalPrice(50.0);
        order.setWeightKg(5.5);
        order.setSize(Order.Size.MEDIUM);
        order.setOrderConfirmedTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        order.setId(orderRepository.save(order).getId());

        Order orderTwo = new Order();
        orderTwo.setOrderId("orderId1234");
        orderTwo.setStatus(Order.Status.DELIVERING);
        orderTwo.setStore(store);
        orderTwo.setCourier(courier);
        orderTwo.setCountry("Georgia");
        orderTwo.setCity("Tbilisi");
        orderTwo.setAddress("Address 45");
        orderTwo.setPhone("+995322235323");
        orderTwo.setZipCode("0186");
        orderTwo.setFullName("FirstName LastName");
        orderTwo.setTrackingNumber("I entered this");
        orderTwo.setDeliveryPrice(20.0);
        orderTwo.setTotalPrice(125.0);
        orderTwo.setWeightKg(6.0);
        orderTwo.setSize(Order.Size.MEDIUM);
        orderTwo.setOrderConfirmedTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        orderTwo.setId(orderRepository.save(orderTwo).getId());

        Specification<Order> orderSpecification = (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("city"),"Yerevan"));
            predicates.add(criteriaBuilder.equal(root.get("country"),"Armenia"));
            predicates.add(criteriaBuilder.equal(root.get("status"),Order.Status.SHIPPED));
            predicates.add(criteriaBuilder.equal(root.get("zipCode"),"0015"));
            predicates.add(criteriaBuilder.equal(root.get("size"),Order.Size.MEDIUM));
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("deliveryPrice"), order.getDeliveryPrice()-1));
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("deliveryPrice"),order.getDeliveryPrice()+1));
            predicates.add(criteriaBuilder.greaterThan(root.get("totalPrice"), order.getTotalPrice()-1));
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("totalPrice"), order.getTotalPrice()+1));
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("weightKg"), order.getWeightKg()-1));
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("weightKg"),order.getWeightKg()+1));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Page<Order> orders = orderRepository.findAll(orderSpecification,PageRequest.of(0,5));
        assertEquals(1,orders.getNumberOfElements());
        Order filteredOrder = orders.getContent().get(0);
        assertEquals(order,filteredOrder);

        Specification<Order> orderTwoSpecification = (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("city"),"Tbilisi"));
            predicates.add(criteriaBuilder.equal(root.get("country"),"Georgia"));
            predicates.add(criteriaBuilder.equal(root.get("status"),Order.Status.DELIVERING));
            predicates.add(criteriaBuilder.equal(root.get("zipCode"),"0186"));
            predicates.add(criteriaBuilder.equal(root.get("size"),Order.Size.MEDIUM));
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("deliveryPrice"), orderTwo.getDeliveryPrice()-1));
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("deliveryPrice"),orderTwo.getDeliveryPrice()+1));
            predicates.add(criteriaBuilder.greaterThan(root.get("totalPrice"), orderTwo.getTotalPrice()-1));
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("totalPrice"), orderTwo.getTotalPrice()+1));
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("weightKg"), orderTwo.getWeightKg()-1));
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("weightKg"),orderTwo.getWeightKg()+1));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Page<Order> ordersTwo = orderRepository.findAll(orderTwoSpecification,PageRequest.of(0,5));
        assertEquals(1,orders.getNumberOfElements());
        Order filteredOrderTwo = ordersTwo.getContent().get(0);
        assertEquals(orderTwo,filteredOrderTwo);
    }
    @Test
    void testFindAllWithInvalidSpecification(){
        User storeAdmin = new User();
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin12345");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        storeAdmin.setId(userRepository.save(storeAdmin).getId());

        User courier = new User();
        courier.setEmail("courier@gmail.com");
        courier.setPassword("courier12345");
        courier.setRole(User.Role.ROLE_COURIER);
        courier.setId(userRepository.save(courier).getId());

        User anotherCourier = new User();
        anotherCourier.setEmail("anotherCourier@gmail.com");
        anotherCourier.setPassword("anotherCourier12345");
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
        order.setPhone("+37477777777");
        order.setZipCode("0015");
        order.setFullName("FirstName LastName");
        order.setTrackingNumber("I entered this");
        order.setDeliveryPrice(10.0);
        order.setTotalPrice(50.0);
        order.setWeightKg(5.5);
        order.setSize(Order.Size.MEDIUM);
        order.setOrderConfirmedTime(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        order.setId(orderRepository.save(order).getId());

        Specification<Order> orderSpecification = (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("city"),"Lion"));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Page<Order> orders = orderRepository.findAll(orderSpecification,PageRequest.of(0,5));
        assertEquals(0,orders.getNumberOfElements());

        Specification<Order> orderSpecification1 = (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("country"),"France"));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Page<Order> orders1 = orderRepository.findAll(orderSpecification1,PageRequest.of(0,5));
        assertEquals(0,orders1.getNumberOfElements());

        Specification<Order> orderSpecification2 = (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("status"),Order.Status.NEW));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Page<Order> orders2 = orderRepository.findAll(orderSpecification2,PageRequest.of(0,5));
        assertEquals(0,orders2.getNumberOfElements());

        Specification<Order> orderSpecification3 = (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("zipCode"),"4015"));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Page<Order> orders3 = orderRepository.findAll(orderSpecification3,PageRequest.of(0,5));
        assertEquals(0,orders3.getNumberOfElements());

        Specification<Order> orderSpecification4 = (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("size"),Order.Size.EXTRA_LARGE));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Page<Order> orders4 = orderRepository.findAll(orderSpecification4,PageRequest.of(0,5));
        assertEquals(0,orders4.getNumberOfElements());

        Specification<Order> orderSpecification5 = (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("deliveryPrice"), order.getDeliveryPrice()+1));
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("deliveryPrice"),order.getDeliveryPrice()-1));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Page<Order> orders5 = orderRepository.findAll(orderSpecification5,PageRequest.of(0,5));
        assertEquals(0,orders5.getNumberOfElements());

        Specification<Order> orderSpecification6 = (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.greaterThan(root.get("totalPrice"), order.getTotalPrice()+1));
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("totalPrice"), order.getTotalPrice()-1));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Page<Order> orders6 = orderRepository.findAll(orderSpecification6,PageRequest.of(0,5));
        assertEquals(0,orders6.getNumberOfElements());

        Specification<Order> orderSpecification7 = (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("weightKg"), order.getWeightKg()+1));
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("weightKg"),order.getWeightKg()-1));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Page<Order> orders7 = orderRepository.findAll(orderSpecification7,PageRequest.of(0,5));
        assertEquals(0,orders7.getNumberOfElements());
    }
}