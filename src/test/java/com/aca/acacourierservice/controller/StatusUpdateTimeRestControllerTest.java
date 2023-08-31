package com.aca.acacourierservice.controller;

import com.aca.acacourierservice.converter.StatusUpdateTimeConverter;
import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.entity.StatusUpdateTime;
import com.aca.acacourierservice.model.StatusUpdateTimeJson;
import com.aca.acacourierservice.service.StatusUpdateTimeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(StatusUpdateTimeRestController.class)
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration")
class StatusUpdateTimeRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StatusUpdateTimeConverter statusUpdateTimeConverter;
    @MockBean
    private StatusUpdateTimeService statusUpdateTimeService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Test
    void testAddStatusUpdateTime() throws Exception {
        when(statusUpdateTimeService.addStatusUpdateTime(any(StatusUpdateTimeJson.class))).thenReturn(1L);
        StatusUpdateTimeJson requestJson = new StatusUpdateTimeJson();
        requestJson.setOrderId(1L);
        requestJson.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST,31,12,39,25));
        requestJson.setUpdatedFrom(Order.Status.SHIPPED);
        requestJson.setUpdatedTo(Order.Status.CANCELLED);
        requestJson.setAdditionalInfo("Some info");
        objectMapper.registerModule(new JavaTimeModule());
        String requestBody =objectMapper.writeValueAsString(requestJson);
        mockMvc.perform(post("/order-status-update-time/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("StatusUpdateTime id=1 created"))
        ;
    }
    @Test
    void testGetStatusUpdateTimeList() throws Exception {
        when(statusUpdateTimeService.getStatusUpdateTimeListByOrderId(1L)).thenReturn(Arrays.asList(
                new StatusUpdateTime(),new StatusUpdateTime()
        ));
        when(statusUpdateTimeConverter.convertToModel(any(StatusUpdateTime.class))).thenReturn(new StatusUpdateTimeJson());
        mockMvc.perform(get("/order-status-update-time/history/orderId/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalCount").value(2));
    }
}