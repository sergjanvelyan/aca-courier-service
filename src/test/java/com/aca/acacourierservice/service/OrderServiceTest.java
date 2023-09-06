package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.OrderConverter;
import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.model.OrderJson;
import com.aca.acacourierservice.repository.OrderRepository;
import com.aca.acacourierservice.repository.StoreRepository;
import com.aca.acacourierservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private OrderConverter orderConverter;
    @Mock
    private StatusUpdateTimeService statusUpdateTimeService;
    @Mock
    private UserService userService;
    @InjectMocks
    private OrderService orderService;

    @Test
    void testAddOrder() {
        User storeAdmin = new User();
        storeAdmin.setId(1L);
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        userRepository.save(storeAdmin);

        User courier = new User();
        courier.setId(2L);
        courier.setEmail("courier@gmail.com");
        courier.setPassword("courier");
        courier.setRole(User.Role.ROLE_COURIER);
        userRepository.save(courier);

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

        OrderJson orderJson = new OrderJson();

        when(orderConverter.convertToEntity(orderJson)).thenReturn(order);
        long id = orderService.addOrder(orderJson);
        assertEquals(1,id);
        verify(orderRepository,times(1)).save(order);
        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderArgumentCaptor.capture());
        Order addedOrder = orderArgumentCaptor.getValue();
        assertThat(addedOrder).isEqualTo(order);
    }

    @Test
    void testUpdateOrderStatus() {
    }

    @Test
    void testAssignCourierToOrder() {
    }

    @Test
    void getOrderById() {
    }

    @Test
    void testGetOrders() {
    }

    @Test
    void testGetOrdersByCourierId() {
    }

    @Test
    void testGetUnassignedOrders() {
    }

    @Test
    void testGetOrdersByStoreId() {
    }
}