package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.model.StoreJson;
import com.aca.acacourierservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;

@ExtendWith(SpringExtension.class)
public class StoreConverterUnitTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private StoreConverter storeConverter;

    @BeforeEach
    public void setUp() {
        lenient().when(userRepository.findById(anyLong())).thenAnswer(invocation -> {
            Long capturedId = invocation.getArgument(0);
            User user = new User();
            user.setId(capturedId);
            return Optional.of(user);
        });
    }

    @Test
    public void testConvertToEntity() {
        StoreJson model = new StoreJson();
        model.setStoreUrl("test.com");
        model.setName("test store");
        model.setPhoneNumber("+123456789");
        model.setAdmin(new User());
        model.setApiSecret("apisecret");
        model.setApiKey("apikey");

        Store entity = storeConverter.convertToEntity(model);
        assertThat(entity).isNotNull();
        assertEquals(entity.getName(), model.getName());
        assertEquals(entity.getStoreUrl(), model.getStoreUrl());
        assertEquals(entity.getApiKey(), model.getApiKey());
        assertEquals(entity.getApiSecret(), model.getApiSecret());
        assertEquals(entity.getAdmin(), model.getAdmin());
    }

    @Test
    public void testConvertToGivenEntity() {
        StoreJson model = new StoreJson();
        model.setStoreUrl("test.com");
        model.setName("test store");
        model.setPhoneNumber("+123456789");
        model.setAdmin(new User());
        model.setApiSecret("apisecret");
        model.setApiKey("apikey");

        Store entity = new Store();
        entity = storeConverter.convertToEntity(model, entity);

        assertThat(entity).isNotNull();
        assertEquals(entity.getName(), model.getName());
        assertEquals(entity.getStoreUrl(), model.getStoreUrl());
        assertEquals(entity.getApiKey(), model.getApiKey());
        assertEquals(entity.getApiSecret(), model.getApiSecret());
        assertEquals(entity.getAdmin(), model.getAdmin());
    }

    @Test
    public void testConvertToModel() {
        Store entity = new Store();
        User user = new User();
        user.setId(1L);

        entity.setStoreUrl("test.com");
        entity.setName("test store");
        entity.setPhoneNumber("+123456789");
        entity.setApiSecret("apisecret");
        entity.setApiKey("apikey");
        entity.setAdmin(user);

        StoreJson model = storeConverter.convertToModel(entity);
        assertThat(model).isNotNull();
        assertEquals(model.getName(), entity.getName());
        assertEquals(model.getStoreUrl(), entity.getStoreUrl());
        assertEquals(model.getApiKey(), entity.getApiKey());
        assertEquals(model.getApiSecret(), entity.getApiSecret());
        assertEquals(model.getAdmin(), entity.getAdmin());
    }
}
