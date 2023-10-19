package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.model.PickupPointJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class PickupPointConverterTest {
    @InjectMocks
    private PickupPointConverter pickupPointConverter;
    @Test
    public void testConvertToEntityWithEntity() {
        PickupPointJson model = new PickupPointJson();
        model.setCity("Yerevan");
        model.setCountry("Armenia");
        model.setAddress("Address 12");
        model.setPhoneNumber("+37499990099");
        model.setZipCode("612");

        PickupPoint entity = new PickupPoint();
        pickupPointConverter.convertToEntity(model, entity);

        assertEquals(model.getCity(), entity.getCity());
        assertEquals(model.getCountry(), entity.getCountry());
        assertEquals(model.getAddress(), entity.getAddress());
        assertEquals(model.getPhoneNumber(), entity.getPhoneNumber());
        assertEquals(model.getZipCode(), entity.getZipCode());
    }
    @Test
    public void testConvertToEntityWithEntityWithNullFieldsInModel() {
        PickupPointJson model = new PickupPointJson();

        PickupPoint entity = new PickupPoint();
        entity.setCity("Yerevan");
        entity.setCountry("Armenia");
        entity.setAddress("Address 12");
        entity.setPhoneNumber("+374999900-9");
        entity.setZipCode("612");

        entity = pickupPointConverter.convertToEntity(model, entity);

        assertNotNull(entity.getCity());
        assertNotNull(entity.getCountry());
        assertNotNull(entity.getAddress());
        assertNotNull(entity.getPhoneNumber());
        assertNotNull(entity.getZipCode());
    }
    @Test
    public void testConvertToEntityWithoutEntity() {
        PickupPointJson model = new PickupPointJson();
        model.setCity("Yerevan");
        model.setCountry("Armenia");
        model.setAddress("Address 12");
        model.setPhoneNumber("+37499990099");
        model.setZipCode("612");

        PickupPoint entity = pickupPointConverter.convertToEntity(model);

        assertEquals(model.getCity(), entity.getCity());
        assertEquals(model.getCountry(), entity.getCountry());
        assertEquals(model.getAddress(), entity.getAddress());
        assertEquals(model.getPhoneNumber(), entity.getPhoneNumber());
        assertEquals(model.getZipCode(), entity.getZipCode());
    }
    @Test
    public void testConvertToModel() {
        Store store = new Store();
        store.setId(1L);

        PickupPoint entity = new PickupPoint();
        entity.setStore(store);
        entity.setCity("Yerevan");
        entity.setCountry("Armenia");
        entity.setAddress("Address 12");
        entity.setPhoneNumber("+37499990099");
        entity.setZipCode("612");

        PickupPointJson model = pickupPointConverter.convertToModel(entity);

        assertEquals(entity.getStore().getId(), model.getStoreId());
        assertEquals(entity.getCity(), model.getCity());
        assertEquals(entity.getCountry(), model.getCountry());
        assertEquals(entity.getAddress(), model.getAddress());
        assertEquals(entity.getPhoneNumber(), model.getPhoneNumber());
        assertEquals(entity.getZipCode(), model.getZipCode());
    }
    @Test
    public void testConvertToEntityList(){
        PickupPointJson pickupPointJsonOne = new PickupPointJson();
        pickupPointJsonOne.setCity("Yerevan");
        pickupPointJsonOne.setCountry("Armenia");
        pickupPointJsonOne.setAddress("Address 12");
        pickupPointJsonOne.setPhoneNumber("+37499990099");
        pickupPointJsonOne.setZipCode("7895");

        PickupPointJson pickupPointJsonTwo = new PickupPointJson();
        pickupPointJsonTwo.setCity("Ijevan");
        pickupPointJsonTwo.setCountry("Armenia");
        pickupPointJsonTwo.setAddress("Yerevanyan 12");
        pickupPointJsonTwo.setPhoneNumber("+37499468591");
        pickupPointJsonTwo.setZipCode("1385");

        List<PickupPointJson> pickupPointJsonList = Arrays.asList(pickupPointJsonOne,pickupPointJsonTwo);
        List<PickupPoint> pickupPoints = pickupPointConverter.convertToEntityList(pickupPointJsonList);

        assertEquals(pickupPointJsonList.get(0).getCity(), pickupPoints.get(0).getCity());
        assertEquals(pickupPointJsonList.get(0).getCountry(), pickupPoints.get(0).getCountry());
        assertEquals(pickupPointJsonList.get(0).getAddress(), pickupPoints.get(0).getAddress());
        assertEquals(pickupPointJsonList.get(0).getPhoneNumber(), pickupPoints.get(0).getPhoneNumber());
        assertEquals(pickupPointJsonList.get(0).getZipCode(), pickupPoints.get(0).getZipCode());

        assertEquals(pickupPointJsonList.get(1).getCity(), pickupPoints.get(1).getCity());
        assertEquals(pickupPointJsonList.get(1).getCountry(), pickupPoints.get(1).getCountry());
        assertEquals(pickupPointJsonList.get(1).getAddress(), pickupPoints.get(1).getAddress());
        assertEquals(pickupPointJsonList.get(1).getPhoneNumber(), pickupPoints.get(1).getPhoneNumber());
        assertEquals(pickupPointJsonList.get(1).getZipCode(), pickupPoints.get(1).getZipCode());
    }
    @Test
    public void testConvertToModelList(){
        PickupPoint pickupPointOne = new PickupPoint();
        pickupPointOne.setCity("Yerevan");
        pickupPointOne.setCountry("Armenia");
        pickupPointOne.setAddress("Address 12");
        pickupPointOne.setPhoneNumber("+374999900-9");
        pickupPointOne.setZipCode("612");

        PickupPoint pickupPointTwo = new PickupPoint();
        pickupPointTwo.setCity("Yerevan");
        pickupPointTwo.setCountry("Armenia");
        pickupPointTwo.setAddress("Address 12");
        pickupPointTwo.setPhoneNumber("+374999900-9");
        pickupPointTwo.setZipCode("612");

        List<PickupPoint> pickupPointList = Arrays.asList(pickupPointOne,pickupPointTwo);
        List<PickupPointJson> pickupPointJsonList = pickupPointConverter.convertToModelList(pickupPointList);

        assertEquals(pickupPointList.get(0).getCity(), pickupPointJsonList.get(0).getCity());
        assertEquals(pickupPointList.get(0).getCountry(), pickupPointJsonList.get(0).getCountry());
        assertEquals(pickupPointList.get(0).getAddress(), pickupPointJsonList.get(0).getAddress());
        assertEquals(pickupPointList.get(0).getPhoneNumber(), pickupPointJsonList.get(0).getPhoneNumber());
        assertEquals(pickupPointList.get(0).getZipCode(), pickupPointJsonList.get(0).getZipCode());

        assertEquals(pickupPointList.get(1).getCity(), pickupPointJsonList.get(1).getCity());
        assertEquals(pickupPointList.get(1).getCountry(), pickupPointJsonList.get(1).getCountry());
        assertEquals(pickupPointList.get(1).getAddress(), pickupPointJsonList.get(1).getAddress());
        assertEquals(pickupPointList.get(1).getPhoneNumber(), pickupPointJsonList.get(1).getPhoneNumber());
        assertEquals(pickupPointList.get(1).getZipCode(), pickupPointJsonList.get(1).getZipCode());
    }
}
