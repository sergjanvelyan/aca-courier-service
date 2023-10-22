package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.StoreConverter;
import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.StoreJson;
import com.aca.acacourierservice.model.UserJson;
import com.aca.acacourierservice.repository.PickupPointRepository;
import com.aca.acacourierservice.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class StoreServiceUnitTest {
    @Mock
    private StoreConverter storeConverter;
    @Mock
    private UserService userService;
    @Mock
    private PickupPointRepository pickupPointRepository;
    @Mock
    private StoreRepository storeRepository;
    @InjectMocks
    private StoreService storeService;

    @Test
    public void testGetStoreById() {
        long storeId = 1L;
        Store store = new Store();
        store.setId(storeId);
        store.setStoreUrl("test.com");
        store.setName("test store");
        store.setPhoneNumber("+37455684524");
        store.setAdmin(new User());
        store.setApiSecret("apiSecret");
        store.setApiKey("apiKey");

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

        Store returnedStore = storeService.getStoreById(storeId);

        assertEquals(store,returnedStore);
    }

    @Test
    public void testGetStoreByInvalidId() {
        long invalidId = 0;
        when(storeRepository.findById(invalidId)).thenReturn(Optional.empty());

        CourierServiceException exception = assertThrows(CourierServiceException.class, () ->
                storeService.getStoreById(invalidId));

        assertEquals("There is no store with id=" + invalidId + ":",exception.getMessage());
    }
    @Test
    public void testGetStoreByApiKey(){
        String apiKey = "apiKey";
        Store store = new Store();
        store.setId(1L);
        store.setStoreUrl("test.com");
        store.setName("test store");
        store.setPhoneNumber("+37498653545");
        store.setAdmin(new User());
        store.setApiSecret("apiSecret");
        store.setApiKey(apiKey);

        when(storeRepository.findByApiKey(apiKey)).thenReturn(Optional.of(store));

        Store returnedStore = storeService.getStoreByApiKey(apiKey);

        assertEquals(store,returnedStore);
    }
    @Test
    public void testGetStoreByInvalidApiKey() {
        String invalidApiKey = "InvalidApiKey";
        when(storeRepository.findByApiKey(invalidApiKey)).thenReturn(Optional.empty());

        CourierServiceException exception = assertThrows(CourierServiceException.class, () ->
                storeService.getStoreByApiKey(invalidApiKey));

        assertEquals("There is no store with apiKey=" + invalidApiKey + ":",exception.getMessage());
    }
    @Test
    public void getStoreByAdminUsername(){
        String adminEmail = "storeAdmin@gmail.com";
        User storeAdmin = new User();
        storeAdmin.setEmail(adminEmail);
        Store store = new Store();
        store.setId(1L);
        store.setStoreUrl("test.com");
        store.setName("test store");
        store.setPhoneNumber("+37499564826");
        store.setAdmin(storeAdmin);
        store.setApiSecret("apiSecret");
        store.setApiKey("apiKey");

        when(storeRepository.findByAdmin_Email(adminEmail)).thenReturn(Optional.of(store));

        Store returnedStore = storeService.getStoreByAdminUsername(adminEmail);

        assertEquals(store,returnedStore);
    }
    @Test
    public void getStoreByInvalidAdminUsername(){
        String invalidAdminEmail = "invalidStoreAdmin@gmail.com";

        CourierServiceException exception = assertThrows(CourierServiceException.class, () ->
                storeService.getStoreByAdminUsername(invalidAdminEmail));

        assertEquals("There is no store with admin username=" + invalidAdminEmail + ":",exception.getMessage());
    }

    @Test
    public void testAddStoreAndAdmin() throws NoSuchAlgorithmException {
        User storeAdmin = new User();
        storeAdmin.setId(1L);
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin12345");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        PickupPoint pickupPoint = new PickupPoint();
        pickupPoint.setId(1L);
        pickupPoint.setCity("Yerevan");
        pickupPoint.setZipCode("5004");
        pickupPoint.setCountry("Armenia");
        pickupPoint.setAddress("Address 45");
        pickupPoint.setPhoneNumber("+37433569845");
        List<PickupPoint> pickupPoints = List.of(pickupPoint);
        StoreJson storeJson = new StoreJson();
        storeJson.setAdmin(new UserJson());
        Store store = new Store();
        store.setId(1L);
        store.setName("Test store");
        store.setStoreUrl("Test.com");
        store.setApiKey("apiKey");
        store.setApiSecret("apiSecret");
        store.setPhoneNumber("+37455896458");
        store.setPickupPoints(pickupPoints);

        when(storeConverter.convertToEntity(storeJson)).thenReturn(store);
        when(userService.saveUser(any(UserJson.class))).thenReturn(storeAdmin);

        Store returnedStore = storeService.addStoreAndAdmin(storeJson);

        verify(storeConverter,times(1)).convertToEntity(storeJson);
        verify(userService,times(1)).saveUser(any(UserJson.class));
        assertEquals(storeAdmin,store.getAdmin());
        verify(pickupPointRepository,times(pickupPoints.size())).save(any(PickupPoint.class));
        verify(storeRepository,times(1)).save(store);
        assertEquals(store,returnedStore);
    }
    @Test
    public void testUpdateStore() throws CourierServiceException {
        long storeId = 1L;
        StoreJson storeJson = new StoreJson();
        Store store = new Store();
        store.setId(1L);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(storeConverter.convertToEntity(storeJson, store)).thenReturn(store);

        storeService.updateStore(storeId, storeJson);

        verify(storeRepository, times(1)).findById(storeId);
        verify(storeConverter, times(1)).convertToEntity(storeJson, store);
        verify(storeRepository, times(1)).save(store);
    }

    @Test
    public void testUpdateStoreWithInvalidId() {
        long invalidStoreId = 1L;
        StoreJson storeJson = new StoreJson();

        when(storeRepository.findById(invalidStoreId)).thenReturn(Optional.empty());

        CourierServiceException exception = assertThrows(CourierServiceException.class, () ->
                storeService.updateStore(invalidStoreId, storeJson));
        assertEquals("There is no store with id=" + invalidStoreId + ":",exception.getMessage());
        verify(storeRepository,times(1)).findById(invalidStoreId);
        verifyNoMoreInteractions(storeConverter,storeRepository);
    }
    @Test
    public void testChangeStoreAdmin() throws CourierServiceException {
        long storeId = 1L;
        UserJson newAdminJson = new UserJson();
        User newAdmin = new User();
        newAdmin.setId(2L);
        User previousAdmin = new User();
        previousAdmin.setId(1L);
        Store store = new Store();
        store.setId(storeId);
        store.setAdmin(previousAdmin);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(userService.saveUser(newAdminJson)).thenReturn(newAdmin);

        User result = storeService.changeStoreAdmin(newAdminJson, storeId);

        verify(storeRepository, times(1)).findById(storeId);
        verify(userService, times(1)).saveUser(newAdminJson);
        verify(userService, times(1)).deleteExistingUserById(previousAdmin.getId());
        verify(storeRepository, times(1)).save(store);
        assertEquals(result,newAdmin);
    }

    @Test
    public void testChangeStoreAdminWithInvalidStoreId() {
        long invalidStoreId = 1L;
        UserJson adminJson = new UserJson();

        when(storeRepository.findById(invalidStoreId)).thenReturn(Optional.empty());

        CourierServiceException exception = assertThrows(CourierServiceException.class, () ->
                storeService.changeStoreAdmin(adminJson, invalidStoreId));
        assertEquals("There is no store with id=" + invalidStoreId + ":",exception.getMessage());
        verify(storeRepository, times(1)).findById(invalidStoreId);
        verifyNoMoreInteractions(userService,storeRepository);
    }
    @Test
    public void testListStoresByPage() {
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        Store storeOne = new Store();
        Store storeTwo = new Store();
        List<Store> storeList = new ArrayList<>(List.of(storeOne,storeTwo));
        Page<Store> storePage = new PageImpl<>(storeList);

        when(storeRepository.findAll(pageRequest)).thenReturn(storePage);

        when(storeConverter.convertToModel(any(Store.class))).thenReturn(new StoreJson());

        List<StoreJson> result = storeService.listStoresByPage(page, size);

        verify(storeRepository, times(1)).findAll(pageRequest);
        verify(storeConverter, times(storeList.size())).convertToModel(any(Store.class));
        assertEquals(storePage.getTotalElements(),result.size());
    }
    @Test
    public void testDeleteStoreById() throws CourierServiceException {
        long storeId = 1L;

        when(storeRepository.existsById(storeId)).thenReturn(true);

        storeService.deleteStoreById(storeId);

        verify(storeRepository, times(1)).existsById(storeId);
        verify(storeRepository, times(1)).deleteById(storeId);
    }
    @Test
    public void testDeleteStoreByInvalidId() {
        long invalidStoreId = 1L;

        when(storeRepository.existsById(invalidStoreId)).thenReturn(false);

        CourierServiceException exception = assertThrows(CourierServiceException.class, () ->
                storeService.deleteStoreById(invalidStoreId));
        assertEquals("There is no store",exception.getMessage());
        verify(storeRepository, times(1)).existsById(invalidStoreId);
        verify(storeRepository, never()).deleteById(invalidStoreId);
    }
}
