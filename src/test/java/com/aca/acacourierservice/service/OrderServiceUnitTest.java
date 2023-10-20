package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.OrderConverter;
import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.ItemOrderInfo;
import com.aca.acacourierservice.model.OrderJson;
import com.aca.acacourierservice.model.StatusInfoJson;
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
import java.util.ArrayList;
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
    private StoreService storeService;
    @Mock
    private StatusUpdateTimeService statusUpdateTimeService;
    @Mock
    private UserService userService;
    @InjectMocks
    private OrderService orderService;

    @Test
    void testAddOrder() {
        Store store = new Store();
        store.setId(1L);
        OrderJson orderJson = new OrderJson();
        orderJson.setStoreId(store.getId());
        Order order = new Order();
        order.setId(1L);
        order.setStore(store);

        when(orderConverter.convertToEntity(orderJson)).thenReturn(order);
        when(storeService.getStoreById(anyLong())).thenReturn(store);
        when(orderRepository.save(order)).thenReturn(order);

        LocalDateTime beforeAddOrder = LocalDateTime.now(ZoneId.of("Asia/Yerevan"));
        orderService.addOrder(orderJson);

        ArgumentCaptor<OrderJson> orderJsonArgumentCaptor = ArgumentCaptor.forClass(OrderJson.class);
        verify(orderConverter,times(1)).convertToEntity(orderJsonArgumentCaptor.capture());
        OrderJson orderJsonCaptured = orderJsonArgumentCaptor.getValue();
        assertEquals(orderJson,orderJsonCaptured);

        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(storeService,times(1)).getStoreById(longArgumentCaptor.capture());
        Long capturedLong = longArgumentCaptor.getValue();
        assertEquals(store.getId(),capturedLong);

        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository,times(1)).save(orderArgumentCaptor.capture());
        Order addedOrder = orderArgumentCaptor.getValue();
        assertThat(addedOrder.getOrderConfirmedTime())
                .isAfterOrEqualTo(beforeAddOrder)
                .isBeforeOrEqualTo(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));
        assertEquals(order,addedOrder);

        ArgumentCaptor<StatusUpdateTimeJson> statusUpdateTimeJsonArgumentCaptor = ArgumentCaptor.forClass(StatusUpdateTimeJson.class);
        verify(statusUpdateTimeService,times(1)).addStatusUpdateTime(statusUpdateTimeJsonArgumentCaptor.capture());
        StatusUpdateTimeJson statusUpdateTimeJson = statusUpdateTimeJsonArgumentCaptor.getValue();

        assertEquals(Order.Status.NEW,statusUpdateTimeJson.getUpdatedTo());
        assertEquals(addedOrder.getOrderConfirmedTime(),statusUpdateTimeJson.getUpdateTime());
    }
    @Test
    public void testUpdateOrderStatus() {
        long orderId = 1L;
        StatusInfoJson statusInfoJson = new StatusInfoJson("DELIVERING","Some info");
        User courier = new User();
        courier.setId(1L);
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(Order.Status.SHIPPED);
        order.setCourier(courier);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        LocalDateTime beforeUpdateOrderStatus = LocalDateTime.now(ZoneId.of("Asia/Yerevan"));
        orderService.updateOrderStatus(orderId, statusInfoJson);

        verify(orderRepository, times(1)).findById(orderId);

        ArgumentCaptor<StatusUpdateTimeJson> argumentCaptor = ArgumentCaptor.forClass(StatusUpdateTimeJson.class);
        verify(statusUpdateTimeService,times(1)).addStatusUpdateTime(argumentCaptor.capture());
        StatusUpdateTimeJson capturedStatusUpdateTimeJson = argumentCaptor.getValue();
        assertEquals(orderId,capturedStatusUpdateTimeJson.getOrderId());
        assertEquals(Order.Status.SHIPPED,capturedStatusUpdateTimeJson.getUpdatedFrom());
        assertEquals(statusInfoJson.getStatus(),capturedStatusUpdateTimeJson.getUpdatedTo());
        assertEquals(statusInfoJson.getAdditionalInfo(),capturedStatusUpdateTimeJson.getAdditionalInfo());
        assertThat(capturedStatusUpdateTimeJson.getUpdateTime())
                .isAfterOrEqualTo(beforeUpdateOrderStatus)
                .isBeforeOrEqualTo(LocalDateTime.now(ZoneId.of("Asia/Yerevan")));

        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderArgumentCaptor.capture());
        Order capturedOrder = orderArgumentCaptor.getValue();
        assertEquals(capturedStatusUpdateTimeJson.getUpdatedTo(),capturedOrder.getStatus());

        assertEquals(statusInfoJson.getStatus(), order.getStatus());
        if (statusInfoJson.getStatus() == Order.Status.DELIVERED) {
            assertNotNull(order.getOrderDeliveredTime());
        } else {
            assertNull(order.getOrderDeliveredTime());
        }
    }
    @Test
    public void testUpdateOrderStatusSameStatus() {
        long orderId = 1L;
        StatusInfoJson statusInfoJson = new StatusInfoJson("SHIPPED","Some info");
        User courier = new User();
        courier.setId(1L);
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(statusInfoJson.getStatus());
        order.setCourier(courier);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        CourierServiceException exception = assertThrows(CourierServiceException.class, () ->
                orderService.updateOrderStatus(orderId, statusInfoJson));
        assertEquals("Can't update to same status", exception.getMessage());
        verifyNoMoreInteractions(orderRepository,statusUpdateTimeService);
    }
    @Test
    public void testUpdateOrderStatusInvalidStatusOrder() {
        long orderId = 1L;
        StatusInfoJson statusInfoJson = new StatusInfoJson("SHIPPED","Some info");
        User courier = new User();
        courier.setId(1L);
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(Order.Status.DELIVERED);
        order.setCourier(courier);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        CourierServiceException exception = assertThrows(CourierServiceException.class, () ->
                orderService.updateOrderStatus(orderId, statusInfoJson));
        assertEquals("Can't update status "+order.getStatus()+" to status "+statusInfoJson.getStatus(), exception.getMessage());
        verifyNoMoreInteractions(orderRepository,statusUpdateTimeService);
    }
    @Test
    public void testUpdateOrderStatusUnassignedOrder() {
        long orderId = 1L;
        StatusInfoJson statusInfoJson = new StatusInfoJson("SHIPPED","Some info");
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(Order.Status.NEW);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        CourierServiceException exception = assertThrows(CourierServiceException.class, () ->
                orderService.updateOrderStatus(orderId, statusInfoJson));
        assertEquals("Can't update unassigned order status", exception.getMessage());
        verifyNoMoreInteractions(orderRepository,statusUpdateTimeService);
    }
    @Test
    void testUpdateOrderStatusWithInvalidOrderId(){
        long orderId = 1L;
        StatusInfoJson statusInfoJson = new StatusInfoJson("SHIPPED","Some info");
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        CourierServiceException exception = assertThrows(CourierServiceException.class, () ->
                orderService.updateOrderStatus(orderId, statusInfoJson));
        assertEquals("Order not found", exception.getMessage());
        verifyNoMoreInteractions(orderRepository,statusUpdateTimeService);
    }
    @Test
    public void testAssignCourierToOrder() {
        long orderId = 1L;
        long courierId = 2L;
        Order order = new Order();
        User courier = new User();
        courier.setRole(User.Role.ROLE_COURIER);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userService.getUserById(courierId)).thenReturn(courier);

        orderService.assignCourierToOrder(orderId, courierId);

        assertEquals(courier, order.getCourier());
        verify(orderRepository, times(1)).findById(orderId);
        verify(userService, times(1)).getUserById(courierId);
        verify(orderRepository, times(1)).save(order);
    }
    @Test
    public void testAssignCourierToNonExistentOrder() {
        long orderId = 1L;
        long courierId = 2L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        CourierServiceException exception = assertThrows(CourierServiceException.class, () ->
                orderService.assignCourierToOrder(orderId, courierId));
        assertEquals("Can't assign nonexistent order to courier", exception.getMessage());

        verify(orderRepository, times(1)).findById(orderId);
        verifyNoMoreInteractions(userService, orderRepository);
    }
    @Test
    public void testAssignAlreadyAssignedOrder() {
        long orderId = 1L;
        long courierId = 2L;
        Order order = new Order();
        order.setCourier(new User());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        CourierServiceException exception = assertThrows(CourierServiceException.class, () ->
                orderService.assignCourierToOrder(orderId, courierId));

        assertEquals("Order already assigned,try to assign another order", exception.getMessage());

        verify(orderRepository, times(1)).findById(orderId);
        verifyNoMoreInteractions(userService, orderRepository);
    }
    @Test
    public void testAssignOrderToNonExistentCourier() {
        long orderId = 1L;
        long courierId = 2L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(new Order()));
        when(userService.getUserById(courierId)).thenThrow(new CourierServiceException());

        CourierServiceException exception = assertThrows(CourierServiceException.class, () ->
                orderService.assignCourierToOrder(orderId, courierId));

        assertEquals("Can't assign order to nonexistent courier", exception.getMessage());

        verify(orderRepository, times(1)).findById(orderId);
        verify(userService, times(1)).getUserById(courierId);
        verifyNoMoreInteractions(orderRepository);
    }
    @Test
    public void testAssignOrderToNonCourierUser() {
        long orderId = 1L;
        long courierId = 2L;
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        User storeAdmin = new User();
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);

        when(userService.getUserById(courierId)).thenReturn(storeAdmin);

        CourierServiceException exception = assertThrows(CourierServiceException.class, () ->
                orderService.assignCourierToOrder(orderId, courierId));

        assertEquals("Can't assign order(id=" + orderId + ") to a user other than courier", exception.getMessage());

        verify(orderRepository, times(1)).findById(orderId);
        verify(userService, times(1)).getUserById(courierId);
        verifyNoMoreInteractions(orderRepository);
    }
    @Test
    public void testGetOrderById() {
        long orderId = 1L;
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Order retrievedOrder = orderService.getOrderById(orderId);

        assertEquals(order, retrievedOrder);
        verify(orderRepository, times(1)).findById(orderId);
    }
    @Test
    public void testGetOrderByInvalidId() {
        long invalidId = 2L;
        when(orderRepository.findById(invalidId)).thenReturn(Optional.empty());

        CourierServiceException exception = assertThrows(CourierServiceException.class, () ->
                orderService.getOrderById(invalidId));

        assertEquals("Order not found", exception.getMessage());
        verify(orderRepository, times(1)).findById(invalidId);
    }
    @Test
    public void testGetOrders() {
        int page = 0;
        int size = 10;
        long orderOneId = 1L;
        Order orderOne = new Order();
        orderOne.setId(orderOneId);
        long orderTwoId = 2L;
        Order orderTwo = new Order();
        orderTwo.setId(orderTwoId);
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Order> orders = Arrays.asList(orderOne,orderTwo);
        Page<Order> orderPage = new PageImpl<>(orders, pageRequest, orders.size());

        when(orderRepository.findAll(pageRequest)).thenReturn(orderPage);

        Page<Order> result = orderService.getOrders(page, size);

        assertEquals(orderPage, result);
        verify(orderRepository, times(1)).findAll(pageRequest);
    }
    @Test
    void testGetOrdersByCourierId() {
        int page = 0;
        int size = 10;
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
        PageRequest pageRequest = PageRequest.of(page, size);

        when(orderRepository.findAllByCourierId(courierId,pageRequest)).thenReturn(courierOrdersPage);

        Page<Order> courierOrdersResultPage = orderService.getOrdersByCourierId(courierId,page,size);

        assertEquals(courierOrdersPage,courierOrdersResultPage);
        verify(orderRepository,times(1)).findAllByCourierId(courierId,pageRequest);
    }
    @Test
    void testGetOrdersByInvalidCourierId() {
        int page = 0;
        int size = 10;
        long courierId = 1L;
        PageRequest pageRequest = PageRequest.of(page, size);

        when(orderRepository.findAllByCourierId(courierId,pageRequest)).thenReturn(Page.empty());

        Page<Order> courierOrdersResultPage = orderService.getOrdersByCourierId(courierId,page,size);

        assertTrue(courierOrdersResultPage.isEmpty());
        verify(orderRepository,times(1)).findAllByCourierId(courierId,pageRequest);
    }
    @Test
    void testGetUnassignedOrders() {
        int page = 0;
        int size = 10;
        long orderOneId = 1L;
        Order orderOne = new Order();
        orderOne.setId(orderOneId);
        long orderTwoId = 2L;
        Order orderTwo = new Order();
        orderTwo.setId(orderTwoId);
        List<Order> unassignedOrders = Arrays.asList(orderOne,orderTwo);
        Page<Order> unassignedOrdersPage = new PageImpl<>(unassignedOrders);
        PageRequest pageRequest = PageRequest.of(page, size);

        when(orderRepository.findAllByCourierIsNull(pageRequest)).thenReturn(unassignedOrdersPage);

        Page<Order> unassignedOrdersResultPage = orderService.getUnassignedOrders(page,size);

        assertEquals(unassignedOrdersPage,unassignedOrdersResultPage);
        verify(orderRepository,times(1)).findAllByCourierIsNull(pageRequest);
    }
    @Test
    void testGetUnassignedOrdersWhenAllAssigned() {
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);

        when(orderRepository.findAllByCourierIsNull(pageRequest)).thenReturn(Page.empty());

        Page<Order> unassignedOrdersResultPage = orderService.getUnassignedOrders(page,size);

        assertTrue(unassignedOrdersResultPage.isEmpty());
        verify(orderRepository,times(1)).findAllByCourierIsNull(pageRequest);
    }
    @Test
    void testGetOrdersByStoreAdminId() {
        long storeAdminId = 1L;
        int page = 0;
        int size = 10;
        User storeAdmin = new User();
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        storeAdmin.setId(storeAdminId);
        long storeId = 1L;
        Store store = new Store();
        store.setId(storeId);
        store.setAdmin(storeAdmin);
        long orderOneId = 1L;
        Order orderOne = new Order();
        orderOne.setId(orderOneId);
        orderOne.setStore(store);
        long orderTwoId = 2L;
        Order orderTwo = new Order();
        orderTwo.setId(orderTwoId);
        orderTwo.setStore(store);
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Order> storeOrders = Arrays.asList(orderOne,orderTwo);
        Page<Order> storeOrdersPage = new PageImpl<>(storeOrders);

        when(orderRepository.findAllByStore_Admin_Id(storeAdminId,pageRequest)).thenReturn(storeOrdersPage);

        Page<Order> storeOrdersResultPage = orderService.getOrdersByStoreAdminId(storeAdminId,page,size);

        assertEquals(storeOrdersPage,storeOrdersResultPage);
        verify(orderRepository,times(1)).findAllByStore_Admin_Id(storeAdminId,pageRequest);
    }
    @Test
    void testGetOrdersByInvalidStoreAdminId() {
        long storeAdminId = 1L;
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);

        when(orderRepository.findAllByStore_Admin_Id(storeAdminId,pageRequest)).thenReturn(Page.empty());

        Page<Order> storeOrdersResultPage = orderService.getOrdersByStoreAdminId(storeAdminId,page,size);

        assertTrue(storeOrdersResultPage.isEmpty());
        verify(orderRepository,times(1)).findAllByStore_Admin_Id(storeAdminId,pageRequest);
    }
    @Test
    public void testCalculateDeliveryPriceWithSameCountry() {
        String pickupPointCountry = "Armenia";
        String destinationCountry = "Armenia";
        ItemOrderInfo itemOrderInfo = new ItemOrderInfo();
        itemOrderInfo.setSize("SMALL");
        itemOrderInfo.setCountry(destinationCountry);
        itemOrderInfo.setWeightKg(2.2);
        long storeId = 1L;
        PickupPoint pickupPoint = new PickupPoint();
        pickupPoint.setCountry(pickupPointCountry);
        List<PickupPoint> pickupPoints = List.of(pickupPoint);
        Store store = new Store();
        store.setPickupPoints(pickupPoints);

        when(storeService.getStoreById(storeId)).thenReturn(store);

        double deliveryPrice = orderService.calculateDeliveryPrice(itemOrderInfo, storeId);

        double expectedPrice = OrderService.STANDARD_FEE_USD  + itemOrderInfo.getWeightKg() * OrderService.DELIVERY_PRICE_USD_KG * OrderService.FEE_COEFFICIENT_OF_SMALL_SIZE;
        assertEquals(expectedPrice, deliveryPrice, 0.01);
    }
    @Test
    public void testCalculateDeliveryPriceWithDifferentCountry() {
        String pickupPointCountry = "Armenia";
        String destinationCountry = "Georgia";
        ItemOrderInfo itemOrderInfo = new ItemOrderInfo();
        itemOrderInfo.setSize("SMALL");
        itemOrderInfo.setCountry(destinationCountry);
        itemOrderInfo.setWeightKg(2.2);
        long storeId = 1L;
        PickupPoint pickupPoint = new PickupPoint();
        pickupPoint.setCountry(pickupPointCountry);
        List<PickupPoint> pickupPoints = List.of(pickupPoint);
        Store store = new Store();
        store.setPickupPoints(pickupPoints);

        when(storeService.getStoreById(storeId)).thenReturn(store);

        double deliveryPrice = orderService.calculateDeliveryPrice(itemOrderInfo, storeId);

        double expectedPrice = OrderService.STANDARD_FEE_USD + 2 + itemOrderInfo.getWeightKg() * OrderService.DELIVERY_PRICE_USD_KG * OrderService.FEE_COEFFICIENT_OF_SMALL_SIZE;
        assertEquals(expectedPrice, deliveryPrice, 0.01);
    }
    @Test
    public void testIsSameCountryWhenSameCountry() {
        List<PickupPoint> pickupPoints = new ArrayList<>();
        PickupPoint pickupPointOne = new PickupPoint();
        pickupPointOne.setCountry("Armenia");
        pickupPoints.add(pickupPointOne);
        PickupPoint pickupPointTwo = new PickupPoint();
        pickupPointTwo.setCountry("Spain");
        pickupPoints.add(pickupPointTwo);
        String destinationCountry = "Spain";

        boolean result = orderService.isSameCountry(pickupPoints, destinationCountry);

        assertTrue(result);
    }
    @Test
    public void testIsSameCountryWithNonMatchingCountry() {
        List<PickupPoint> pickupPoints = new ArrayList<>();
        PickupPoint pickupPointOne = new PickupPoint();
        pickupPointOne.setCountry("Canada");
        pickupPoints.add(pickupPointOne);
        PickupPoint pickupPointTwo = new PickupPoint();
        pickupPointTwo.setCountry("Germany");
        pickupPoints.add(pickupPointTwo);
        String destinationCountry = "Armenia";

        boolean result = orderService.isSameCountry(pickupPoints, destinationCountry);

        assertFalse(result);
    }
}