package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.entity.StatusUpdateTime;
import com.aca.acacourierservice.model.StatusUpdateTimeJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class StatusUpdateTimeConverterTest {
    @InjectMocks
    private StatusUpdateTimeConverter statusUpdateTimeConverter;

    @Test
    void testConvertToEntityWithEntity() {
        StatusUpdateTimeJson model = new StatusUpdateTimeJson();
        model.setUpdatedTo(Order.Status.DELIVERED);
        model.setUpdatedFrom(Order.Status.DELIVERING);
        model.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST,30,1,30,56));
        model.setAdditionalInfo("Additional info");

        StatusUpdateTime entity = new StatusUpdateTime();
        StatusUpdateTime result = statusUpdateTimeConverter.convertToEntity(model,entity);

        assertThat(result).isNotNull();
        assertEquals(model.getUpdateTime(),entity.getUpdateTime());
        assertEquals(model.getUpdatedTo(),entity.getUpdatedTo());
        assertEquals(model.getUpdatedFrom(),entity.getUpdatedFrom());
        assertEquals(model.getAdditionalInfo(),entity.getAdditionalInfo());
    }
    @Test
    void testConvertToEntityWithoutEntity() {
        StatusUpdateTimeJson model = new StatusUpdateTimeJson();
        model.setUpdatedTo(Order.Status.DELIVERED);
        model.setUpdatedFrom(Order.Status.DELIVERING);
        model.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST,30,1,30,56));
        model.setAdditionalInfo("Additional info");

        StatusUpdateTime entity = statusUpdateTimeConverter.convertToEntity(model);

        assertThat(entity).isNotNull();
        assertEquals(model.getUpdateTime(),entity.getUpdateTime());
        assertEquals(model.getUpdatedTo(),entity.getUpdatedTo());
        assertEquals(model.getUpdatedFrom(),entity.getUpdatedFrom());
        assertEquals(model.getAdditionalInfo(),entity.getAdditionalInfo());
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

    @Test
    void testConvertToListModel() {
        Order order = new Order();
        order.setId(1);
        StatusUpdateTime entityOne = new StatusUpdateTime();
        entityOne.setOrder(order);
        entityOne.setUpdatedTo(Order.Status.DELIVERING);
        entityOne.setUpdatedFrom(Order.Status.SHIPPED);
        entityOne.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST,29,1,30,56));
        entityOne.setAdditionalInfo("Additional info");

        StatusUpdateTime entityTwo = new StatusUpdateTime();
        entityTwo.setOrder(order);
        entityTwo.setUpdatedTo(Order.Status.DELIVERED);
        entityTwo.setUpdatedFrom(Order.Status.DELIVERING);
        entityTwo.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST,30,1,30,56));
        entityTwo.setAdditionalInfo("Additional info");

        List<StatusUpdateTimeJson> listModel = statusUpdateTimeConverter.convertToListModel(Arrays.asList(entityOne,entityTwo));


        assertEquals(2,listModel.size());

        StatusUpdateTimeJson modelOne = listModel.get(0);
        assertEquals(entityOne.getUpdateTime(),modelOne.getUpdateTime());
        assertEquals(entityOne.getUpdatedTo(),modelOne.getUpdatedTo());
        assertEquals(entityOne.getUpdatedFrom(),modelOne.getUpdatedFrom());
        assertEquals(entityOne.getAdditionalInfo(),modelOne.getAdditionalInfo());
        assertEquals(entityOne.getOrder().getId(),modelOne.getOrderId());

        StatusUpdateTimeJson modelTwo = listModel.get(1);
        assertEquals(entityTwo.getUpdateTime(),modelTwo.getUpdateTime());
        assertEquals(entityTwo.getUpdatedTo(),modelTwo.getUpdatedTo());
        assertEquals(entityTwo.getUpdatedFrom(),modelTwo.getUpdatedFrom());
        assertEquals(entityTwo.getAdditionalInfo(),modelTwo.getAdditionalInfo());
        assertEquals(entityTwo.getOrder().getId(),modelTwo.getOrderId());
    }
}