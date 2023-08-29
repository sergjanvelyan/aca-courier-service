package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.entity.StatusUpdateTime;
import com.aca.acacourierservice.model.StatusUpdateTimeJson;
import com.aca.acacourierservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class StatusUpdateTimeConverterTest {
    @Mock
    private OrderService orderService;
    @InjectMocks
    private StatusUpdateTimeConverter statusUpdateTimeConverter;

    @BeforeEach
    void setUp() {
        lenient().when(orderService.getOrderById(anyLong())).thenAnswer(invocation -> {
            Long capturedId = invocation.getArgument(0);
            Order order = new Order();
            order.setId(capturedId);
            return order;
        });
    }
    @Test
    void testConvertToEntityWithEntity() {
        StatusUpdateTimeJson model = new StatusUpdateTimeJson();
        model.setUpdatedTo(Order.Status.DELIVERED);
        model.setUpdatedFrom(Order.Status.DELIVERING);
        model.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST,30,1,30,56));
        model.setAdditionalInfo("Additional info");
        model.setOrderId(1);
        StatusUpdateTime entity = new StatusUpdateTime();
        StatusUpdateTime result = statusUpdateTimeConverter.convertToEntity(model,entity);
        assertThat(result).isNotNull();
        assertEquals(model.getUpdateTime(),entity.getUpdateTime());
        assertEquals(model.getUpdatedTo(),entity.getUpdatedTo());
        assertEquals(model.getUpdatedFrom(),entity.getUpdatedFrom());
        assertEquals(model.getAdditionalInfo(),entity.getAdditionalInfo());
        assertEquals(model.getOrderId(),entity.getOrder().getId());
    }
    @Test
    void testConvertToEntityWithoutEntity() {
        StatusUpdateTimeJson model = new StatusUpdateTimeJson();
        model.setUpdatedTo(Order.Status.DELIVERED);
        model.setUpdatedFrom(Order.Status.DELIVERING);
        model.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST,30,1,30,56));
        model.setAdditionalInfo("Additional info");
        model.setOrderId(1);
        StatusUpdateTime entity = statusUpdateTimeConverter.convertToEntity(model);
        assertThat(entity).isNotNull();
        assertEquals(model.getUpdateTime(),entity.getUpdateTime());
        assertEquals(model.getUpdatedTo(),entity.getUpdatedTo());
        assertEquals(model.getUpdatedFrom(),entity.getUpdatedFrom());
        assertEquals(model.getAdditionalInfo(),entity.getAdditionalInfo());
        assertEquals(model.getOrderId(),entity.getOrder().getId());
    }
    @Test
    void convertToModel() {
        StatusUpdateTime entity = new StatusUpdateTime();
        Order order = new Order();
        order.setId(1);
        entity.setOrder(order);
        entity.setUpdatedTo(Order.Status.DELIVERED);
        entity.setUpdatedFrom(Order.Status.DELIVERING);
        entity.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST,30,1,30,56));
        entity.setAdditionalInfo("Additional info");
        StatusUpdateTimeJson model = statusUpdateTimeConverter.convertToModel(entity);
        assertThat(model).isNotNull();
        assertEquals(entity.getUpdateTime(),model.getUpdateTime());
        assertEquals(entity.getUpdatedTo(),model.getUpdatedTo());
        assertEquals(entity.getUpdatedFrom(),model.getUpdatedFrom());
        assertEquals(entity.getAdditionalInfo(),model.getAdditionalInfo());
        assertEquals(entity.getOrder().getId(),model.getOrderId());
    }
}