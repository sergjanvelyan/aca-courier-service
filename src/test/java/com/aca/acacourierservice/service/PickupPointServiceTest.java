package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.PickupPointConverter;
import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.PickupPointJson;
import com.aca.acacourierservice.repository.PickupPointRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PickupPointServiceTest {
    @Mock
    private  PickupPointRepository pickupPointRepository;
    @Mock
    private  PickupPointConverter pickupPointConverter;
    @Mock
    private StoreService storeService;
    @InjectMocks
    private PickupPointService pickupPointService;

    @Test
    public void testGetPickupPointById() {
        long id = 1L;
        PickupPoint pickupPoint = new PickupPoint();
        when(pickupPointRepository.findById(id)).thenReturn(Optional.of(pickupPoint));

        PickupPoint result = pickupPointService.getPickupPointById(id);

        assertNotNull(result);
        assertEquals(pickupPoint, result);
    }
    @Test
    public void testGetPickupPointByInvalidId() {
        long invalidId = 1L;
        when(pickupPointRepository.findById(invalidId)).thenReturn(Optional.empty());

        CourierServiceException exception =assertThrows(CourierServiceException.class, () ->
                pickupPointService.getPickupPointById(invalidId));
        assertEquals("Pickup point not found",exception.getMessage());
    }
    @Test
    public void testAddPickupPoint() {
        long storeId = 1L;
        PickupPointJson pickupPointJson = new PickupPointJson();
        pickupPointJson.setStoreId(storeId);
        Store store = new Store();
        store.setId(storeId);
        PickupPoint pickupPoint = new PickupPoint();
        when(pickupPointConverter.convertToEntity(pickupPointJson)).thenReturn(pickupPoint);
        when(storeService.getStoreById(storeId)).thenReturn(store);
        when(pickupPointRepository.save(pickupPoint)).thenReturn(pickupPoint);

        long resultId = pickupPointService.addPickupPoint(pickupPointJson).getId();

        assertEquals(pickupPoint.getId(), resultId);
    }
    @Test
    void testModifyPickupPoint() {
        long id = 1L;
        String email = "test@gmail.com";
        PickupPointJson pickupPointJson = new PickupPointJson();
        PickupPoint pickupPoint = new PickupPoint();

        when(pickupPointRepository.findByIdAndStore_Admin_Email(id, email)).thenReturn(Optional.of(pickupPoint));
        when(pickupPointConverter.convertToEntity(pickupPointJson, pickupPoint)).thenReturn(pickupPoint);

        pickupPointService.modifyPickupPoint(id, email, pickupPointJson);

        verify(pickupPointRepository, times(1)).findByIdAndStore_Admin_Email(id, email);
        verify(pickupPointConverter, times(1)).convertToEntity(pickupPointJson,pickupPoint);
        verify(pickupPointRepository, times(1)).save(any(PickupPoint.class));
    }
    @Test
    void testModifyPickupPointByInvalidId() {
        long invalidId = 1L;
        String email = "test@gmail.com";
        PickupPointJson pickupPointJson = new PickupPointJson();

        when(pickupPointRepository.findByIdAndStore_Admin_Email(invalidId, email)).thenReturn(Optional.empty());

        CourierServiceException exception = assertThrows(CourierServiceException.class,
                () -> pickupPointService.modifyPickupPoint(invalidId, email, pickupPointJson));

        assertEquals("Pickup point for this store not found", exception.getMessage());
        verify(pickupPointRepository, times(1)).findByIdAndStore_Admin_Email(invalidId, email);
        verifyNoMoreInteractions(pickupPointRepository,pickupPointConverter);
    }
    @Test
    void testModifyPickupPointByInvalidEmail() {
        long id = 1L;
        String invalidEmail = "invalid@gmail.com";
        PickupPointJson pickupPointJson = new PickupPointJson();

        when(pickupPointRepository.findByIdAndStore_Admin_Email(id, invalidEmail)).thenReturn(Optional.empty());

        CourierServiceException exception = assertThrows(CourierServiceException.class,
                () -> pickupPointService.modifyPickupPoint(id, invalidEmail, pickupPointJson));

        assertEquals("Pickup point for this store not found", exception.getMessage());
        verify(pickupPointRepository, times(1)).findByIdAndStore_Admin_Email(id, invalidEmail);
        verifyNoMoreInteractions(pickupPointRepository,pickupPointConverter);
    }
    @Test
    void testDeletePickupPoint() {
        long id = 1L;
        String email = "test@gmail.com";
        when(pickupPointRepository.existsByIdAndStore_Admin_Email(id, email)).thenReturn(true);
        pickupPointService.deletePickupPoint(id, email);
        verify(pickupPointRepository, times(1)).existsByIdAndStore_Admin_Email(id, email);
        verify(pickupPointRepository, times(1)).deleteById(id);
    }
    @Test
    void testDeletePickupPointByInvalidId() {
        long invalidId = 1L;
        String email = "test@gmail.com";
        when(pickupPointRepository.existsByIdAndStore_Admin_Email(invalidId, email)).thenReturn(false);
        CourierServiceException exception = assertThrows(CourierServiceException.class,
                () -> pickupPointService.deletePickupPoint(invalidId, email));
        assertEquals("Pickup point for this store not found", exception.getMessage());
        verify(pickupPointRepository, times(1)).existsByIdAndStore_Admin_Email(invalidId, email);
        verifyNoMoreInteractions(pickupPointRepository,pickupPointConverter);
    }
}