package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.StoreConverter;
import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.StoreJson;
import com.aca.acacourierservice.repository.PickupPointRepository;
import com.aca.acacourierservice.repository.StoreRepository;
import com.aca.acacourierservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class StoreServiceUnitTest {
    @Mock
    private StoreConverter storeConverter;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PickupPointRepository pickupPointRepository;
    @Mock
    private StoreRepository storeRepository;
    @InjectMocks
    private StoreService storeService;

//    @Test
//    public void testGetStoreById() {
//        Store store = new Store();
//        store.setId(5L);
//        store.setStoreUrl("test.com");
//        store.setName("test store");
//        store.setPhoneNumber("+123456789");
//        store.setAdmin(new User());
//        store.setApiSecret("apisecret");
//        store.setApiKey("apikey");
//
//        when(storeRepository.findById(anyLong())).thenReturn(Optional.of(store));
//        Store storeById = storeService.getStoreById(5L);
//        assertNotNull(storeById);
//        assertEquals(storeById.getName(), store.getName());
//        assertEquals(storeById.getStoreUrl(), store.getStoreUrl());
//        assertEquals(storeById.getPhoneNumber(), store.getPhoneNumber());
//        assertEquals(storeById.getApiKey(), store.getApiKey());
//        assertEquals(storeById.getApiSecret(), store.getApiSecret());
//        assertEquals(storeById.getAdmin(), store.getAdmin());
//    }
//
//    @Test
//    public void testGetStoreByInvalidId() {
//        when(storeRepository.findById(anyLong())).thenReturn(Optional.empty());
//        RuntimeException exception = assertThrows(CourierServiceException.class, () -> storeService.getStoreById(15L));
//        assertTrue(exception.getMessage().contains("There is no store with id "));
//    }
//
//    @Test
//    public void testAddStore() {
//        User admin = new User();
//        admin.setId(1L);
//        admin.setEmail("some@email.com");
//        admin.setPassword("password123$");
//        admin.setRole(User.Role.ROLE_STORE_ADMIN);
//        userRepository.save(admin);
//
//        PickupPoint pickupPoint = new PickupPoint();
//        pickupPoint.setId(1L);
//        pickupPoint.setCity("city");
//        pickupPoint.setZipCode("5004");
//        pickupPoint.setCountry("country");
//        pickupPoint.setAddress("some address");
//        pickupPoint.setPhoneNumber("+123456789");
//        pickupPointRepository.save(pickupPoint);
//
//        List<PickupPoint> pickupPoints = new ArrayList<>();
//        pickupPoints.add(pickupPoint);
//
//        Store store = new Store();
//        store.setId(1L);
//        store.setName("test store");
//        store.setStoreUrl("test.com");
//        store.setApiKey("apikey");
//        store.setApiSecret("apisecret");
//        store.setPhoneNumber("+123456789");
//        store.setAdmin(admin);
//        store.setPickupPoints(pickupPoints);
//
//        StoreJson storeJson = new StoreJson();
//
//        when(storeConverter.convertToEntity(storeJson)).thenReturn(store);
//
//        long id = storeService.addStore(storeJson);
//        assertEquals(id, store.getId());
//
//        verify(storeRepository, times(1)).save(store);
//
//        ArgumentCaptor<Store> storeArgumentCaptor = ArgumentCaptor.forClass(Store.class);
//        verify(storeRepository).save(storeArgumentCaptor.capture());
//        Store addedStore = storeArgumentCaptor.getValue();
//        assertThat(addedStore).isEqualTo(store);
//    }
}
