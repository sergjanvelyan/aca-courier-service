package com.aca.acacourierservice.controller;

import com.aca.acacourierservice.converter.OrderConverter;
import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.ItemOrderInfo;
import com.aca.acacourierservice.model.OrderJson;
import com.aca.acacourierservice.model.StatusInfoJson;
import com.aca.acacourierservice.model.StatusUpdateTimeJson;
import com.aca.acacourierservice.service.OrderService;
import com.aca.acacourierservice.service.StatusUpdateTimeService;
import com.aca.acacourierservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OrderRestControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;
    @MockBean
    private UserService userService;
    @MockBean
    private OrderConverter orderConverter;
    @MockBean
    private StatusUpdateTimeService statusUpdateTimeService;
    @Test
    public void testCreateOrder() throws Exception {
        OrderJson orderJson = new OrderJson();
        orderJson.setOrderId("2");
        orderJson.setFullName("Anna Hovsepyan");
        orderJson.setCountry("Armenia");
        orderJson.setCity("Yerevan");
        orderJson.setAddress("Abovyan 34");
        orderJson.setPhone("+37491600709");
        orderJson.setZipCode("0001");
        orderJson.setWeightKg(4.0);
        orderJson.setSize("SMALL");
        orderJson.setDeliveryPrice(10.2);
        orderJson.setTotalPrice(50.8);
        String trackingNumber = "TrackingNumber";

        when(orderService.addOrder(any(OrderJson.class))).thenReturn(trackingNumber);

        mockMvc.perform(post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderJson))
                        .with(request -> {
                            request.setAttribute("apiKey","apiKey");
                            request.setAttribute("apiSecret","apiSecret");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Created order"))
                .andExpect(jsonPath("$.trackingNumber").value(trackingNumber));
        verify(orderService,times(1)).addOrder(any(OrderJson.class));
    }
    @Test
    public void testGetOrder() throws Exception {
        long orderId = 1L;
        Order order = new Order();
        OrderJson orderJson = new OrderJson();
        orderJson.setOrderId("OrderId");
        when(orderService.getOrderById(orderId)).thenReturn(order);
        when(orderConverter.convertToModel(any(Order.class))).thenReturn(orderJson);

        mockMvc.perform(get("/order/" + orderId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderId").value("OrderId"));
        verify(orderService, times(1)).getOrderById(orderId);
    }
    @Test
    public void testGetOrderWithInvalidOrderId() throws Exception {
        long invalidOrderId = 1L;
        when(orderService.getOrderById(invalidOrderId))
                .thenThrow(new CourierServiceException("Order not found"));
        mockMvc.perform(get("/order/" + invalidOrderId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("Order not found"));
    }
    @Test
    public void testCalculateDeliveryPrice() throws Exception {
        long storeId = 1L;
        ItemOrderInfo itemInfo = new ItemOrderInfo();
        itemInfo.setSize("Small");
        itemInfo.setWeightKg(3);
        itemInfo.setCountry("Armenia");
        double deliveryPrice = 10.0;
        when(orderService.calculateDeliveryPrice(any(ItemOrderInfo.class),anyLong())).thenReturn(deliveryPrice);
        mockMvc.perform(post("/order/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(itemInfo))
                        .with(request -> {
                          request.setAttribute("storeId",storeId);
                         return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.deliveryPrice").value(deliveryPrice))
                .andExpect(jsonPath("$.currency").value("USD"));
        verify(orderService, times(1)).calculateDeliveryPrice(any(ItemOrderInfo.class),anyLong());
    }
    @Test
    public void testCalculateDeliveryPriceWithInvalidInput() throws Exception {
        long storeId = 1L;
        ItemOrderInfo itemInfo = new ItemOrderInfo();
        itemInfo.setWeightKg(3);
        itemInfo.setCountry("Armenia");
        mockMvc.perform(post("/order/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(itemInfo))
                        .with(request -> {
                            request.setAttribute("storeId",storeId);
                            return request;
                        }))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verifyNoInteractions(orderService);
    }
    @Test
    public void testTrackOrder() throws Exception {
        String trackingNumber = "TrackingNumber";
        List<StatusUpdateTimeJson> statusUpdateHistory = new ArrayList<>();
        when(statusUpdateTimeService.getStatusUpdateTimeListByOrderTrackingNumber(trackingNumber)).thenReturn(statusUpdateHistory);
        mockMvc.perform(get("/order/tracking/" + trackingNumber))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(statusUpdateHistory.size())));
        verify(statusUpdateTimeService, times(1)).getStatusUpdateTimeListByOrderTrackingNumber(trackingNumber);
    }
    @Test
    public void testTrackOrderWithInvalidTrackingNumber() throws Exception {
        String invalidTrackingNumber = "InvalidTrackingNumber";
        when(statusUpdateTimeService.getStatusUpdateTimeListByOrderTrackingNumber(invalidTrackingNumber))
                .thenThrow(new CourierServiceException("Invalid tracking ID"));
        mockMvc.perform(get("/order/tracking/" + invalidTrackingNumber))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("Invalid tracking ID")));
    }
    @Test
    public void testUpdateOrderStatus() throws Exception {
        long orderId = 1L;
        StatusInfoJson statusInfoJson = new StatusInfoJson("Delivered","Some info");
        mockMvc.perform(put("/order/" + orderId + "/updateStatus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(statusInfoJson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("Order status updated")));

        verify(orderService, times(1)).updateOrderStatus(anyLong(), any(StatusInfoJson.class));
    }
    @Test
    public void testUpdateOrderStatusWithInvalidOrderId() throws Exception {
        long invalidOrderId = 1L;
        StatusInfoJson statusInfoJson = new StatusInfoJson("Delivered","Some info");
        doThrow(new CourierServiceException("Invalid order ID")).when(orderService).updateOrderStatus(eq(invalidOrderId), any(StatusInfoJson.class));
        mockMvc.perform(put("/order/" + invalidOrderId + "/updateStatus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(statusInfoJson)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("Invalid order ID")));
    }
}
