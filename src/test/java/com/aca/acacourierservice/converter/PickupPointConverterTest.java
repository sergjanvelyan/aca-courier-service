package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.model.PickupPointJson;
import com.aca.acacourierservice.service.StoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PickupPointConverterTest {

    @Mock
    private StoreService storeService;

    @InjectMocks
    private PickupPointConverter pickupPointConverter;


    @Test
    public void testConvertToEntity() {
        PickupPointJson model = new PickupPointJson();
        model.setStoreId(1L);
        model.setCity("Erevan");
        model.setCountry("Armenia");
        model.setAddress("Byuzand 12");
        model.setPhoneNumber("+374-99-99-00-99");
        model.setZipCode("612");

        Store store = new Store();
        store.setId(1L);

        when(storeService.getStoreById(1L)).thenReturn(store);

        PickupPoint entity = new PickupPoint();
        pickupPointConverter.convertToEntity(model, entity);

        assertEquals(1L, entity.getStore().getId());
        assertEquals("Erevan", entity.getCity());
        assertEquals("Armenia", entity.getCountry());
        assertEquals("Byuzand 12", entity.getAddress());
        assertEquals("+374-99-99-00-99", entity.getPhoneNumber());
        assertEquals("612", entity.getZipCode());

        verify(storeService, times(1)).getStoreById(1L);
    }

    @Test
    public void testConvertToModel() {
        // Prepare input data
        Store store = new Store();
        store.setId(1L);

        PickupPoint entity = new PickupPoint();
        entity.setStore(store);
        entity.setCity("Erevan");
        entity.setCountry("Armenia");
        entity.setAddress("Byuzand 12");
        entity.setPhoneNumber("+374-99-99-00-99");
        entity.setZipCode("612");

        PickupPointJson model = pickupPointConverter.convertToModel(entity);

        assertEquals(1L, model.getStoreId());
        assertEquals("Erevan", model.getCity());
        assertEquals("Armenia", model.getCountry());
        assertEquals("Byuzand 12", model.getAddress());
        assertEquals("+374-99-99-00-99", model.getPhoneNumber());
        assertEquals("612", model.getZipCode());
    }
}
