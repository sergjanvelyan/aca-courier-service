package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.model.OrderJson;
import com.aca.acacourierservice.model.OrderListJson;
import com.aca.acacourierservice.service.StoreService;
import com.aca.acacourierservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class OrderConverterTest {
    @Mock
    private StoreService storeService;
    @Mock
    private UserService userService;
    @InjectMocks
    private OrderConverter orderConverter;
    @BeforeEach
    void setUp() {
        lenient().when(userService.getUserById(anyLong())).thenAnswer(invocation -> {
            Long capturedId = invocation.getArgument(0);
            User user = new User();
            user.setId(capturedId);
            return user;
        });
        lenient().when(storeService.getStoreById(anyLong())).thenAnswer(invocation -> {
            Long capturedId = invocation.getArgument(0);
            Store store = new Store();
            store.setId(capturedId);
            return store;
        });
    }
    @Test
    void testConvertToEntityWithEntity() {
        OrderJson model = new OrderJson();
        model.setOrderId("orderId1234");
        model.setStatus(Order.Status.SHIPPED);
        model.setStoreId(1L);
        model.setCourierId(2L);
        model.setCountry("Armenia");
        model.setCity("Yerevan");
        model.setAddress("Address 7");
        model.setPhone("+374-77-77-77-77");
        model.setZipCode("0015");
        model.setFullName("FirstName LastName");
        model.setTrackingNumber("I entered this");
        model.setDeliveryPrice(10);
        model.setTotalPrice(50);
        model.setWeightKg(5.5);
        model.setSize(Order.Size.MEDIUM);
        model.setOrderConfirmedTime(LocalDateTime.of(2023, Month.SEPTEMBER,6,14,40,26));
        model.setOrderDeliveredTime(LocalDateTime.of(2023, Month.SEPTEMBER,15,15,0));
        Order entity = new Order();
        orderConverter.convertToEntity(model,entity);
        assertEquals(model.getOrderId(),entity.getOrderId());
        assertEquals(model.getStatus(),entity.getStatus());
        assertEquals(model.getStoreId(),entity.getStore().getId());
        assertEquals(model.getCourierId(),entity.getCourier().getId());
        assertEquals(model.getCountry(),entity.getCountry());
        assertEquals(model.getCity(),entity.getCity());
        assertEquals(model.getAddress(),entity.getAddress());
        assertEquals(model.getPhone(),entity.getPhone());
        assertEquals(model.getZipCode(),entity.getZipCode());
        assertEquals(model.getFullName(),entity.getFullName());
        assertEquals(model.getTrackingNumber(),entity.getTrackingNumber());
        assertEquals(model.getDeliveryPrice(),entity.getDeliveryPrice());
        assertEquals(model.getTotalPrice(),entity.getTotalPrice());
        assertEquals(model.getWeightKg(),entity.getWeightKg());
        assertEquals(model.getSize(),entity.getSize());
        assertEquals(model.getOrderConfirmedTime(),entity.getOrderConfirmedTime());
        assertEquals(model.getOrderDeliveredTime(),entity.getOrderDeliveredTime());
    }

    @Test
    void testConvertToEntityWithoutEntity() {
        OrderJson model = new OrderJson();
        model.setOrderId("orderId1234");
        model.setStatus(Order.Status.SHIPPED);
        model.setStoreId(1L);
        model.setCourierId(2L);
        model.setCountry("Armenia");
        model.setCity("Yerevan");
        model.setAddress("Address 7");
        model.setPhone("+374-77-77-77-77");
        model.setZipCode("0015");
        model.setFullName("FirstName LastName");
        model.setTrackingNumber("I entered this");
        model.setDeliveryPrice(10);
        model.setTotalPrice(50);
        model.setWeightKg(5.5);
        model.setSize(Order.Size.MEDIUM);
        model.setOrderConfirmedTime(LocalDateTime.of(2023, Month.SEPTEMBER,6,14,40,26));
        model.setOrderDeliveredTime(LocalDateTime.of(2023, Month.SEPTEMBER,15,15,0));
        Order entity = orderConverter.convertToEntity(model);
        assertEquals(model.getOrderId(),entity.getOrderId());
        assertEquals(model.getStatus(),entity.getStatus());
        assertEquals(model.getStoreId(),entity.getStore().getId());
        assertEquals(model.getCourierId(),entity.getCourier().getId());
        assertEquals(model.getCountry(),entity.getCountry());
        assertEquals(model.getCity(),entity.getCity());
        assertEquals(model.getAddress(),entity.getAddress());
        assertEquals(model.getPhone(),entity.getPhone());
        assertEquals(model.getZipCode(),entity.getZipCode());
        assertEquals(model.getFullName(),entity.getFullName());
        assertEquals(model.getTrackingNumber(),entity.getTrackingNumber());
        assertEquals(model.getDeliveryPrice(),entity.getDeliveryPrice());
        assertEquals(model.getTotalPrice(),entity.getTotalPrice());
        assertEquals(model.getWeightKg(),entity.getWeightKg());
        assertEquals(model.getSize(),entity.getSize());
        assertEquals(model.getOrderConfirmedTime(),entity.getOrderConfirmedTime());
        assertEquals(model.getOrderDeliveredTime(),entity.getOrderDeliveredTime());
    }

    @Test
    void convertToModel() {
        Store store = new Store();
        store.setId(1L);
        User courier = new User();
        courier.setId(1L);
        Order entity = new Order();
        entity.setOrderId("orderId1234");
        entity.setStatus(Order.Status.SHIPPED);
        entity.setStore(store);
        entity.setCourier(courier);
        entity.setCountry("Armenia");
        entity.setCity("Yerevan");
        entity.setAddress("Address 7");
        entity.setPhone("+374-77-77-77-77");
        entity.setZipCode("0015");
        entity.setFullName("FirstName LastName");
        entity.setTrackingNumber("I entered this");
        entity.setDeliveryPrice(10);
        entity.setTotalPrice(50);
        entity.setWeightKg(5.5);
        entity.setSize(Order.Size.MEDIUM);
        entity.setOrderConfirmedTime(LocalDateTime.of(2023, Month.SEPTEMBER,6,14,40,26));
        entity.setOrderDeliveredTime(LocalDateTime.of(2023, Month.SEPTEMBER,15,15,0));
        OrderJson model = orderConverter.convertToModel(entity);
        assertEquals(entity.getOrderId(),model.getOrderId());
        assertEquals(entity.getStatus(),model.getStatus());
        assertEquals(entity.getStore().getId(),model.getStoreId());
        assertEquals(entity.getCourier().getId(),model.getCourierId());
        assertEquals(entity.getCountry(),model.getCountry());
        assertEquals(entity.getCity(),model.getCity());
        assertEquals(entity.getAddress(),model.getAddress());
        assertEquals(entity.getPhone(),model.getPhone());
        assertEquals(entity.getZipCode(),model.getZipCode());
        assertEquals(entity.getFullName(),model.getFullName());
        assertEquals(entity.getTrackingNumber(),model.getTrackingNumber());
        assertEquals(entity.getDeliveryPrice(),model.getDeliveryPrice());
        assertEquals(entity.getTotalPrice(),model.getTotalPrice());
        assertEquals(entity.getWeightKg(),model.getWeightKg());
        assertEquals(entity.getSize(),model.getSize());
        assertEquals(entity.getOrderConfirmedTime(),model.getOrderConfirmedTime());
        assertEquals(entity.getOrderDeliveredTime(),model.getOrderDeliveredTime());
    }

    @Test
    void convertToListModel() {
        Store store = new Store();
        store.setId(1L);
        User courier = new User();
        courier.setId(1L);
        Order orderOne = new Order();
        orderOne.setOrderId("orderId1234");
        orderOne.setStatus(Order.Status.SHIPPED);
        orderOne.setStore(store);
        orderOne.setCourier(courier);
        orderOne.setCountry("Armenia");
        orderOne.setCity("Yerevan");
        orderOne.setAddress("Address 7");
        orderOne.setPhone("+374-77-77-77-77");
        orderOne.setZipCode("0015");
        orderOne.setFullName("FirstName LastName");
        orderOne.setTrackingNumber("I entered this");
        orderOne.setDeliveryPrice(10);
        orderOne.setTotalPrice(50);
        orderOne.setWeightKg(5.5);
        orderOne.setSize(Order.Size.MEDIUM);
        orderOne.setOrderConfirmedTime(LocalDateTime.of(2023, Month.SEPTEMBER,6,14,40,26));
        orderOne.setOrderDeliveredTime(LocalDateTime.of(2023, Month.SEPTEMBER,15,15,0));

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
        orderTwo.setOrderConfirmedTime(LocalDateTime.of(2023, Month.SEPTEMBER,6,14,40,26));
        orderTwo.setOrderDeliveredTime(LocalDateTime.of(2023, Month.SEPTEMBER,15,15,0));

        Page<Order> ordersPage = new PageImpl<>(Arrays.asList(orderOne,orderTwo));
        OrderListJson listModel = orderConverter.convertToListModel(ordersPage);
        List<OrderJson> orderJsonList = listModel.getOrderListJson();
        assertEquals(2,orderJsonList.size());
        assertEquals(orderOne.getOrderId(),orderJsonList.get(0).getOrderId());
        assertEquals(orderOne.getStatus(),orderJsonList.get(0).getStatus());
        assertEquals(orderOne.getStore().getId(),orderJsonList.get(0).getStoreId());
        assertEquals(orderOne.getCourier().getId(),orderJsonList.get(0).getCourierId());
        assertEquals(orderOne.getCountry(),orderJsonList.get(0).getCountry());
        assertEquals(orderOne.getCity(),orderJsonList.get(0).getCity());
        assertEquals(orderOne.getAddress(),orderJsonList.get(0).getAddress());
        assertEquals(orderOne.getPhone(),orderJsonList.get(0).getPhone());
        assertEquals(orderOne.getZipCode(),orderJsonList.get(0).getZipCode());
        assertEquals(orderOne.getFullName(),orderJsonList.get(0).getFullName());
        assertEquals(orderOne.getTrackingNumber(),orderJsonList.get(0).getTrackingNumber());
        assertEquals(orderOne.getDeliveryPrice(),orderJsonList.get(0).getDeliveryPrice());
        assertEquals(orderOne.getTotalPrice(),orderJsonList.get(0).getTotalPrice());
        assertEquals(orderOne.getWeightKg(),orderJsonList.get(0).getWeightKg());
        assertEquals(orderOne.getSize(),orderJsonList.get(0).getSize());
        assertEquals(orderOne.getOrderConfirmedTime(),orderJsonList.get(0).getOrderConfirmedTime());
        assertEquals(orderOne.getOrderDeliveredTime(),orderJsonList.get(0).getOrderDeliveredTime());

        assertEquals(2,orderJsonList.size());
        assertEquals(orderTwo.getOrderId(),orderJsonList.get(1).getOrderId());
        assertEquals(orderTwo.getStatus(),orderJsonList.get(1).getStatus());
        assertEquals(orderTwo.getStore().getId(),orderJsonList.get(1).getStoreId());
        assertEquals(orderTwo.getCourier().getId(),orderJsonList.get(1).getCourierId());
        assertEquals(orderTwo.getCountry(),orderJsonList.get(1).getCountry());
        assertEquals(orderTwo.getCity(),orderJsonList.get(1).getCity());
        assertEquals(orderTwo.getAddress(),orderJsonList.get(1).getAddress());
        assertEquals(orderTwo.getPhone(),orderJsonList.get(1).getPhone());
        assertEquals(orderTwo.getZipCode(),orderJsonList.get(1).getZipCode());
        assertEquals(orderTwo.getFullName(),orderJsonList.get(1).getFullName());
        assertEquals(orderTwo.getTrackingNumber(),orderJsonList.get(1).getTrackingNumber());
        assertEquals(orderTwo.getDeliveryPrice(),orderJsonList.get(1).getDeliveryPrice());
        assertEquals(orderTwo.getTotalPrice(),orderJsonList.get(1).getTotalPrice());
        assertEquals(orderTwo.getWeightKg(),orderJsonList.get(1).getWeightKg());
        assertEquals(orderTwo.getSize(),orderJsonList.get(1).getSize());
        assertEquals(orderTwo.getOrderConfirmedTime(),orderJsonList.get(1).getOrderConfirmedTime());
        assertEquals(orderTwo.getOrderDeliveredTime(),orderJsonList.get(1).getOrderDeliveredTime());
    }
}
