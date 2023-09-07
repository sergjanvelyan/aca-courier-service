package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.StatusUpdateTimeConverter;
import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.entity.StatusUpdateTime;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.StatusUpdateTimeJson;
import com.aca.acacourierservice.repository.OrderRepository;
import com.aca.acacourierservice.repository.StatusUpdateTimeRepository;
import com.aca.acacourierservice.repository.StoreRepository;
import com.aca.acacourierservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatusUpdateTimeServiceTest {
    @Mock
    private StatusUpdateTimeRepository statusUpdateTimeRepository;
    @Mock
    private StatusUpdateTimeConverter statusUpdateTimeConverter;
    @Mock
    private UserRepository userRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private StatusUpdateTimeService statusUpdateTimeService;
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

        StatusUpdateTime statusUpdateTime = new StatusUpdateTime();
        statusUpdateTime.setId(1L);
        statusUpdateTime.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST,29,13,10,10));
        statusUpdateTime.setUpdatedFrom(Order.Status.SHIPPED);
        statusUpdateTime.setUpdatedTo(Order.Status.CANCELLED);
        statusUpdateTime.setOrder(order);

        when(statusUpdateTimeConverter.convertToEntity(statusUpdateTimeJson)).thenReturn(statusUpdateTime);

        long id = statusUpdateTimeService.addStatusUpdateTime(statusUpdateTimeJson);
        assertEquals(1,id);

        verify(statusUpdateTimeRepository,times(1)).save(statusUpdateTime);

        ArgumentCaptor<StatusUpdateTime> statusUpdateTimeArgumentCaptor = ArgumentCaptor.forClass(StatusUpdateTime.class);
        verify(statusUpdateTimeRepository).save(statusUpdateTimeArgumentCaptor.capture());
        StatusUpdateTime addedStatusUpdateTime = statusUpdateTimeArgumentCaptor.getValue();
        assertThat(addedStatusUpdateTime).isEqualTo(statusUpdateTime);
    }
    @Test
    public void testGetStatusUpdateTimeListByOrderId(){
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

        StatusUpdateTime firstStatusUpdateTime = new StatusUpdateTime();
        firstStatusUpdateTime.setId(1L);
        firstStatusUpdateTime.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST,29,13,10,10));
        firstStatusUpdateTime.setUpdatedFrom(Order.Status.SHIPPED);
        firstStatusUpdateTime.setUpdatedTo(Order.Status.DELIVERING);
        firstStatusUpdateTime.setOrder(order);

        StatusUpdateTime secondStatusUpdateTime = new StatusUpdateTime();
        secondStatusUpdateTime.setId(2L);
        secondStatusUpdateTime.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST,29,14,46,10));
        secondStatusUpdateTime.setUpdatedFrom(Order.Status.DELIVERED);
        secondStatusUpdateTime.setUpdatedTo(Order.Status.DELIVERING);
        secondStatusUpdateTime.setOrder(order);

        when(statusUpdateTimeRepository.findAllByOrderId(order.getId())).thenReturn(Arrays.asList(firstStatusUpdateTime,secondStatusUpdateTime));
        List<StatusUpdateTime> statusUpdateHistory = statusUpdateTimeService.getStatusUpdateTimeListByOrderId(order.getId());
        assertNotNull(statusUpdateHistory);
        assertFalse(statusUpdateHistory.isEmpty());
        assertEquals(2,statusUpdateHistory.size());
        assertEquals(firstStatusUpdateTime,statusUpdateHistory.get(0));
        assertEquals(secondStatusUpdateTime,statusUpdateHistory.get(1));
    }
    @Test
    public void testGetStatusUpdateTimeListByInvalidOrderId(){
        long orderId=2;
        when(statusUpdateTimeRepository.findAllByOrderId(orderId)).thenReturn(new ArrayList<>());
        RuntimeException exception = assertThrows(CourierServiceException.class, () -> statusUpdateTimeService.getStatusUpdateTimeListByOrderId(orderId));
        assertTrue(exception.getMessage().contains("There is no status update history for order(id="+orderId+")"));
    }
 }