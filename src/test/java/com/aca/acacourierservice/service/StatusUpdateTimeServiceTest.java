package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.StatusUpdateTimeConverter;
import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.entity.StatusUpdateTime;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.StatusUpdateTimeJson;
import com.aca.acacourierservice.repository.StatusUpdateTimeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatusUpdateTimeServiceTest {
    @Mock
    private StatusUpdateTimeRepository statusUpdateTimeRepository;
    @Mock
    private StatusUpdateTimeConverter statusUpdateTimeConverter;
    @Mock
    private OrderService orderService;
    @InjectMocks
    private StatusUpdateTimeService statusUpdateTimeService;
    @Test
    public void testAddStatusUpdateTime(){
        long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        StatusUpdateTimeJson statusUpdateTimeJson = new StatusUpdateTimeJson();
        statusUpdateTimeJson.setOrderId(orderId);
        StatusUpdateTime statusUpdateTime = new StatusUpdateTime();
        statusUpdateTime.setId(1L);
        statusUpdateTime.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST,29,13,10,10));
        statusUpdateTime.setUpdatedFrom(Order.Status.SHIPPED);
        statusUpdateTime.setUpdatedTo(Order.Status.CANCELLED);

        when(statusUpdateTimeConverter.convertToEntity(statusUpdateTimeJson)).thenReturn(statusUpdateTime);
        when(orderService.getOrderById(orderId)).thenReturn(order);

        long id = statusUpdateTimeService.addStatusUpdateTime(statusUpdateTimeJson);
        assertEquals(1,id);

        verify(statusUpdateTimeConverter,times(1)).convertToEntity(statusUpdateTimeJson);
        verify(statusUpdateTimeRepository,times(1)).save(statusUpdateTime);

        ArgumentCaptor<StatusUpdateTime> statusUpdateTimeArgumentCaptor = ArgumentCaptor.forClass(StatusUpdateTime.class);
        verify(statusUpdateTimeRepository).save(statusUpdateTimeArgumentCaptor.capture());
        StatusUpdateTime addedStatusUpdateTime = statusUpdateTimeArgumentCaptor.getValue();
        assertEquals(statusUpdateTime,addedStatusUpdateTime);
    }
    @Test
    public void testGetStatusUpdateTimeListByOrderId(){
        long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        StatusUpdateTime firstStatusUpdateTime = new StatusUpdateTime();
        firstStatusUpdateTime.setId(1L);
        firstStatusUpdateTime.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST,29,13,10,10));
        firstStatusUpdateTime.setUpdatedFrom(Order.Status.SHIPPED);
        firstStatusUpdateTime.setUpdatedTo(Order.Status.DELIVERING);
        firstStatusUpdateTime.setOrder(order);

        StatusUpdateTime secondStatusUpdateTime = new StatusUpdateTime();
        secondStatusUpdateTime.setId(2L);
        secondStatusUpdateTime.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST,29,14,46,10));
        secondStatusUpdateTime.setUpdatedFrom(Order.Status.DELIVERING);
        secondStatusUpdateTime.setUpdatedTo(Order.Status.DELIVERED);
        secondStatusUpdateTime.setOrder(order);
        List<StatusUpdateTime> statusUpdateTimeList = Arrays.asList(firstStatusUpdateTime,secondStatusUpdateTime);

        when(statusUpdateTimeRepository.findAllByOrderId(orderId)).thenReturn(statusUpdateTimeList);

        List<StatusUpdateTime> statusUpdateHistory = statusUpdateTimeService.getStatusUpdateTimeListByOrderId(orderId);

        assertNotNull(statusUpdateHistory);
        assertEquals(statusUpdateTimeList,statusUpdateHistory);
    }

    @Test
    public void testGetStatusUpdateTimeListByInvalidOrderId(){
        long orderId=2;

        when(statusUpdateTimeRepository.findAllByOrderId(orderId)).thenReturn(List.of());

        CourierServiceException exception = assertThrows(CourierServiceException.class, () ->
                statusUpdateTimeService.getStatusUpdateTimeListByOrderId(orderId));

        assertEquals("There is no status update history for order(id="+orderId+")",exception.getMessage());
    }
    @Test
    public void testGetStatusUpdateTimeListByOrderTrackingNumber(){
        String trackingNumber = "SomeTrackingNumber";
        long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setTrackingNumber(trackingNumber);
        StatusUpdateTime firstStatusUpdateTime = new StatusUpdateTime();
        firstStatusUpdateTime.setId(1L);
        firstStatusUpdateTime.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST,29,13,10,10));
        firstStatusUpdateTime.setUpdatedFrom(Order.Status.SHIPPED);
        firstStatusUpdateTime.setUpdatedTo(Order.Status.DELIVERING);
        firstStatusUpdateTime.setOrder(order);
        StatusUpdateTime secondStatusUpdateTime = new StatusUpdateTime();
        secondStatusUpdateTime.setId(2L);
        secondStatusUpdateTime.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST,29,14,46,10));
        secondStatusUpdateTime.setUpdatedFrom(Order.Status.DELIVERING);
        secondStatusUpdateTime.setUpdatedTo(Order.Status.DELIVERED);
        secondStatusUpdateTime.setOrder(order);

        StatusUpdateTimeJson firstStatusUpdateTimeJson = new StatusUpdateTimeJson();
        firstStatusUpdateTimeJson.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST,29,13,10,10));
        firstStatusUpdateTimeJson.setUpdatedFrom(Order.Status.SHIPPED);
        firstStatusUpdateTimeJson.setUpdatedTo(Order.Status.DELIVERING);
        firstStatusUpdateTimeJson.setOrderId(orderId);

        StatusUpdateTimeJson secondStatusUpdateTimeJson = new StatusUpdateTimeJson();
        secondStatusUpdateTimeJson.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST,29,14,46,10));
        secondStatusUpdateTimeJson.setUpdatedFrom(Order.Status.DELIVERING);
        secondStatusUpdateTimeJson.setUpdatedTo(Order.Status.DELIVERED);
        secondStatusUpdateTimeJson.setOrderId(orderId);
        List<StatusUpdateTime> statusUpdateTimeList = Arrays.asList(firstStatusUpdateTime,secondStatusUpdateTime);

        when(statusUpdateTimeRepository.findAllByOrder_TrackingNumber(trackingNumber)).thenReturn(statusUpdateTimeList);
        when(statusUpdateTimeConverter.convertToModel(firstStatusUpdateTime)).thenReturn(firstStatusUpdateTimeJson);
        when(statusUpdateTimeConverter.convertToModel(secondStatusUpdateTime)).thenReturn(secondStatusUpdateTimeJson);

        List<StatusUpdateTimeJson> statusUpdateTimeHistory = statusUpdateTimeService.getStatusUpdateTimeListByOrderTrackingNumber(trackingNumber);

        assertEquals(firstStatusUpdateTimeJson,statusUpdateTimeHistory.get(0));
        assertEquals(secondStatusUpdateTimeJson,statusUpdateTimeHistory.get(1));
        verify(statusUpdateTimeRepository,times(1)).findAllByOrder_TrackingNumber(trackingNumber);
        verify(statusUpdateTimeConverter,times(1)).convertToModel(firstStatusUpdateTime);
        verify(statusUpdateTimeConverter,times(1)).convertToModel(secondStatusUpdateTime);
    }
    @Test
    public void testGetStatusUpdateTimeListByInvalidOrderTrackingNumber(){
        String invalidTrackingNumber = "InvalidTrackingNumber";

        when(statusUpdateTimeRepository.findAllByOrder_TrackingNumber(invalidTrackingNumber)).thenReturn(List.of());

        CourierServiceException exception = assertThrows(CourierServiceException.class, () ->
                statusUpdateTimeService.getStatusUpdateTimeListByOrderTrackingNumber(invalidTrackingNumber));

        assertEquals("There is no order with tracking number ("+invalidTrackingNumber+")",exception.getMessage());

        verify(statusUpdateTimeRepository,times(1)).findAllByOrder_TrackingNumber(invalidTrackingNumber);
        verifyNoMoreInteractions(statusUpdateTimeConverter);
    }
 }