package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.model.PickupPointJson;
import com.aca.acacourierservice.model.StoreJson;
import com.aca.acacourierservice.model.UserJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class StoreConverterUnitTest {
    @Mock
    private UserConverter userConverter;
    @Mock
    private PickupPointConverter pickupPointConverter;
    @InjectMocks
    private StoreConverter storeConverter;

    @Test
    public void testConvertToEntityWithoutEntity() {
        StoreJson model = new StoreJson();
        model.setStoreUrl("ModelTest.com");
        model.setName("ModelTest store");
        model.setPhoneNumber("+37477856423");
        model.setAdmin(new UserJson());
        model.setApiSecret("ModelApiSecret");
        model.setApiKey("ModelApiKey");
        model.setPickupPoints(new ArrayList<>());
        List<PickupPoint> pickupPointList = new ArrayList<>();
        pickupPointList.add(new PickupPoint());
        pickupPointList.add(new PickupPoint());

        User admin = new User();
        when(userConverter.convertToEntity(any(UserJson.class))).thenReturn(admin);
        when(pickupPointConverter.convertToEntityList(any())).thenReturn(pickupPointList);
        Store entity = storeConverter.convertToEntity(model);

        assertNotNull(entity);
        assertEquals(model.getName(), entity.getName());
        assertEquals(model.getStoreUrl(), entity.getStoreUrl());
        assertNull(entity.getApiKey());
        assertNull(entity.getApiSecret());
        assertEquals(model.getPhoneNumber(),entity.getPhoneNumber());
        assertEquals(admin, entity.getAdmin());
        assertEquals(pickupPointList,entity.getPickupPoints());
    }

    @Test
    public void testConvertToEntityWithEntity() {
        StoreJson model = new StoreJson();
        model.setStoreUrl("ModelTest.com");
        model.setName("ModelTest store");
        model.setPhoneNumber("+37477856423");
        model.setAdmin(new UserJson());
        model.setApiSecret("ModelApiSecret");
        model.setApiKey("ModelApiKey");

        User admin = new User();
        when(userConverter.convertToEntity(any(UserJson.class))).thenReturn(new User());
        Store entity = new Store();
        entity.setStoreUrl("EntityTest.com");
        entity.setName("EntityTest store");
        entity.setPhoneNumber("+37477856423");
        entity.setAdmin(admin);
        entity.setApiSecret("EntityApiSecret");
        entity.setApiKey("EntityApiKey");

        entity = storeConverter.convertToEntity(model, entity);

        assertNotNull(entity);
        assertEquals(model.getName(), entity.getName());
        assertEquals(model.getStoreUrl(), entity.getStoreUrl());
        assertEquals("EntityApiKey", entity.getApiKey());
        assertEquals("EntityApiSecret", entity.getApiSecret());
        assertEquals(model.getPhoneNumber(),entity.getPhoneNumber());
        assertEquals(admin, entity.getAdmin());
    }
    @Test
    public void testConvertToEntityWithEntityWithNullFields() {
        StoreJson model = new StoreJson();

        User admin = new User();
        when(userConverter.convertToEntity(any(UserJson.class))).thenReturn(new User());
        Store entity = new Store();
        entity.setStoreUrl("EntityTest.com");
        entity.setName("EntityTest store");
        entity.setPhoneNumber("+37477856423");
        entity.setAdmin(admin);
        entity.setApiSecret("EntityApiSecret");
        entity.setApiKey("EntityApiKey");

        entity = storeConverter.convertToEntity(model, entity);

        assertNotNull(entity);
        assertEquals("EntityTest store", entity.getName());
        assertEquals("EntityTest.com", entity.getStoreUrl());
        assertEquals("EntityApiKey", entity.getApiKey());
        assertEquals("EntityApiSecret", entity.getApiSecret());
        assertEquals("+37477856423",entity.getPhoneNumber());
        assertEquals(admin, entity.getAdmin());
    }
    @Test
    public void testConvertToModel() {
        Store entity = new Store();
        entity.setId(1L);
        entity.setStoreUrl("Test.com");
        entity.setName("Test store");
        entity.setPhoneNumber("+37455916852");
        entity.setApiSecret("apiSecret");
        entity.setApiKey("apiKey");
        entity.setAdmin(new User());
        entity.setPickupPoints(new ArrayList<>());

        List<PickupPointJson> pickupPointJsonList = new ArrayList<>();
        pickupPointJsonList.add(new PickupPointJson());
        pickupPointJsonList.add(new PickupPointJson());

        UserJson userJson = new UserJson();
        when(userConverter.convertToModel(any())).thenReturn(userJson);
        when(pickupPointConverter.convertToModelList(any())).thenReturn(pickupPointJsonList);
        StoreJson model = storeConverter.convertToModel(entity);

        assertNotNull(model);
        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getName(), model.getName());
        assertEquals(entity.getStoreUrl(), model.getStoreUrl());
        assertEquals(entity.getApiKey(), model.getApiKey());
        assertEquals(entity.getApiSecret(), model.getApiSecret());
        assertEquals(entity.getPhoneNumber(),model.getPhoneNumber());
        assertEquals(userJson, model.getAdmin());
        assertEquals(pickupPointJsonList,model.getPickupPoints());
    }
}
