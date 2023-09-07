package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.OrderConverter;
import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.OrderJson;
import com.aca.acacourierservice.model.StatusUpdateTimeJson;
import com.aca.acacourierservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceUnitTest {
    @Mock
    private OrderRepository orderRepository;
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
        long orderId = 1L;
        Order order = new Order();
        order.setId(1L);

        OrderJson orderJson = new OrderJson();

        when(orderConverter.convertToEntity(orderJson)).thenReturn(order);
        LocalDateTime beforeAddOrder = LocalDateTime.now(ZoneId.of("Asia/Yerevan"));
        long id = orderService.addOrder(orderJson);
        assertEquals(orderId,id);
        verify(orderRepository,times(1)).save(order);
        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderArgumentCaptor.capture());
        Order addedOrder = orderArgumentCaptor.getValue();
        assertThat(addedOrder.getOrderConfirmedTime())
                .isAfterOrEqualTo(beforeAddOrder)
                .isBeforeOrEqualTo(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        assertThat(addedOrder).isEqualTo(order);
    }

    @Test
    void testUpdateOrderStatus() {
        long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(Order.Status.SHIPPED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        LocalDateTime beforeUpdateOrderStatus = LocalDateTime.now(ZoneId.of("Asia/Yerevan"));
        orderService.updateOrderStatus(orderId, Order.Status.DELIVERING,"Additional info");

        assertEquals(Order.Status.DELIVERING,order.getStatus());
        ArgumentCaptor<StatusUpdateTimeJson> argumentCaptor = ArgumentCaptor.forClass(StatusUpdateTimeJson.class);
        verify(statusUpdateTimeService,times(1)).addStatusUpdateTime(argumentCaptor.capture());
        StatusUpdateTimeJson capturedStatusUpdateTimeJson = argumentCaptor.getValue();
        assertEquals(orderId,capturedStatusUpdateTimeJson.getOrderId());
        assertEquals(Order.Status.SHIPPED,capturedStatusUpdateTimeJson.getUpdatedFrom());
        assertEquals(Order.Status.DELIVERING,capturedStatusUpdateTimeJson.getUpdatedTo());
        assertEquals("Additional info",capturedStatusUpdateTimeJson.getAdditionalInfo());
        assertThat(capturedStatusUpdateTimeJson.getUpdateTime())
                .isAfterOrEqualTo(beforeUpdateOrderStatus)
                .isBeforeOrEqualTo(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));

        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderArgumentCaptor.capture());
        Order capturedOrder = orderArgumentCaptor.getValue();
        assertEquals(capturedStatusUpdateTimeJson.getUpdatedTo(),capturedOrder.getStatus());
    }
    @Test
    void testUpdateOrderStatusWithInvalidOrderId(){
        long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        Exception e = assertThrows(CourierServiceException.class,()-> orderService.updateOrderStatus(orderId,Order.Status.DELIVERED,"Additional info"));
        assertTrue(e.getMessage().contains("Can't update nonexistent order(id="+orderId+")"));
        verify(statusUpdateTimeService,never()).addStatusUpdateTime(any());
        verify(orderRepository,never()).save(any());
    }
    @Test
    void testAssignCourierToOrder() {
        long courierId = 2L;
        User courier = new User();

        long orderId = 1L;
        Order order = new Order();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userService.getUserById(courierId)).thenReturn(courier);
        orderService.assignCourierToOrder(orderId,courierId);
        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository,times(1)).save(orderArgumentCaptor.capture());
        Order capturedOrder = orderArgumentCaptor.getValue();
        assertEquals(courier,capturedOrder.getCourier());
    }
    @Test
    void testAssignCourierToOrderWithInvalidCourierId() {
        long courierId=1L;
        long orderId = 1L;
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userService.getUserById(courierId))
                .thenThrow(new CourierServiceException("Can't assign order(id="+orderId+")+ to nonexistent courier(id="+courierId+")"));
        Exception e = assertThrows(CourierServiceException.class,()-> orderService.assignCourierToOrder(orderId,courierId));
        assertTrue(e.getMessage().contains("Can't assign order(id="+orderId+")+ to nonexistent courier(id="+courierId+")"));
        verify(orderRepository,never()).save(any());
    }
    @Test
    void testAssignCourierToOrderWithInvalidOrderId() {
        long courierId=1L;
        long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        Exception e = assertThrows(CourierServiceException.class,()-> orderService.assignCourierToOrder(orderId,courierId));
        assertTrue(e.getMessage().contains("Can't assign nonexistent order(id="+orderId+")+ to courier(id="+courierId+")"));
        verify(orderRepository,never()).save(any());
    }
    @Test
    void testGetOrderById() {
        long orderId = 1L;
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        assertEquals(order,orderService.getOrderById(orderId));
    }
    @Test
    void testGetOrderByInvalidId() {
        long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        Exception e = assertThrows(CourierServiceException.class,()-> orderService.getOrderById(orderId));
        assertTrue(e.getMessage().contains("There is no order with id("+orderId+")"));
    }
    @Test
    void testGetOrders() {
        long orderOneId = 1L;
        Order orderOne = new Order();
        orderOne.setId(orderOneId);
        long orderTwoId = 2L;
        Order orderTwo = new Order();
        orderTwo.setId(orderTwoId);
        List<Order> orders = Arrays.asList(orderOne,orderTwo);
        Page<Order> ordersPage = new PageImpl<>(orders);
        when(orderRepository.findAll(PageRequest.of(0,5))).thenReturn(ordersPage);
        Page<Order> ordersResultPage = orderService.getOrders(0,5);
        assertEquals(orders,ordersResultPage.getContent());
        verify(orderRepository,times(1)).findAll(PageRequest.of(0, 5));
    }

    @Test
    void testGetOrdersByCourierId() {
        long courierId = 1L;
        User courier = new User();
        courier.setId(courierId);
        long orderOneId = 1L;
        Order orderOne = new Order();
        orderOne.setId(orderOneId);
        orderOne.setCourier(courier);
        long orderTwoId = 2L;
        Order orderTwo = new Order();
        orderTwo.setId(orderTwoId);
        orderTwo.setCourier(courier);
        List<Order> courierOrders = Arrays.asList(orderOne,orderTwo);
        Page<Order> courierOrdersPage = new PageImpl<>(courierOrders);
        when(orderRepository.findAllByCourierId(courierId,PageRequest.of(0,5))).thenReturn(courierOrdersPage);
        Page<Order> courierOrdersResultPage = orderService.getOrdersByCourierId(courierId,0,5);
        assertEquals(courierOrders,courierOrdersResultPage.getContent());
        verify(orderRepository,times(1)).findAllByCourierId(courierId,PageRequest.of(0,5));
    }
    @Test
    void testGetOrdersByInvalidCourierId() {
        long courierId = 1L;
        when(orderRepository.findAllByCourierId(courierId,PageRequest.of(0,5))).thenReturn(Page.empty());
        Page<Order> courierOrdersResultPage = orderService.getOrdersByCourierId(courierId,0,5);
        assertTrue(courierOrdersResultPage.isEmpty());
        verify(orderRepository,times(1)).findAllByCourierId(courierId,PageRequest.of(0,5));
    }

    @Test
    void testGetUnassignedOrders() {
        long orderOneId = 1L;
        Order orderOne = new Order();
        orderOne.setId(orderOneId);
        long orderTwoId = 2L;
        Order orderTwo = new Order();
        orderTwo.setId(orderTwoId);
        List<Order> unassignedOrders = Arrays.asList(orderOne,orderTwo);
        Page<Order> unassignedOrdersPage = new PageImpl<>(unassignedOrders);
        when(orderRepository.findAllByCourierIsNull(PageRequest.of(0,5))).thenReturn(unassignedOrdersPage);
        Page<Order> unassignedOrdersResultPage = orderService.getUnassignedOrders(0,5);
        assertEquals(unassignedOrders,unassignedOrdersResultPage.getContent());
        verify(orderRepository,times(1)).findAllByCourierIsNull(PageRequest.of(0,5));
    }
    @Test
    void testGetUnassignedOrdersWhenAllAssigned() {
        when(orderRepository.findAllByCourierIsNull(PageRequest.of(0,5))).thenReturn(Page.empty());
        Page<Order> unassignedOrdersResultPage = orderService.getUnassignedOrders(0,5);
        assertTrue(unassignedOrdersResultPage.isEmpty());
        verify(orderRepository,times(1)).findAllByCourierIsNull(PageRequest.of(0,5));
    }

    @Test
    void testGetOrdersByStoreId() {
        long storeId = 1L;
        Store store = new Store();
        store.setId(storeId);
        long orderOneId = 1L;
        Order orderOne = new Order();
        orderOne.setId(orderOneId);
        orderOne.setStore(store);
        long orderTwoId = 2L;
        Order orderTwo = new Order();
        orderTwo.setId(orderTwoId);
        orderTwo.setStore(store);
        List<Order> storeOrders = Arrays.asList(orderOne,orderTwo);
        Page<Order> storeOrdersPage = new PageImpl<>(storeOrders);
        when(orderRepository.findAllByStoreId(storeId,PageRequest.of(0,5))).thenReturn(storeOrdersPage);
        Page<Order> storeOrdersResultPage = orderService.getOrdersByStoreId(storeId,0,5);
        assertEquals(storeOrders,storeOrdersResultPage.getContent());
        verify(orderRepository,times(1)).findAllByStoreId(storeId,PageRequest.of(0,5));
    }
    @Test
    void testGetOrdersByInvalidStoreId() {
        long storeId = 1L;
        when(orderRepository.findAllByStoreId(storeId,PageRequest.of(0,5))).thenReturn(Page.empty());
        Page<Order> storeOrdersResultPage = orderService.getOrdersByStoreId(storeId,0,5);
        assertTrue(storeOrdersResultPage.isEmpty());
        verify(orderRepository,times(1)).findAllByStoreId(storeId,PageRequest.of(0,5));
    }
}