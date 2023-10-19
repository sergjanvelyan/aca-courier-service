package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.model.UserJson;
import com.aca.acacourierservice.model.UserListJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class UserConverterTest {
    @InjectMocks
    private UserConverter userConverter;
    @Test
    void testConvertToEntityWithEntity() {
        UserJson model = new UserJson();
        model.setEmail("admin@gmail.com");
        model.setPassword("admin12345");
        model.setRole("ROLE_ADMIN");
        model.setAddress("Address 45");
        model.setBirthDate("1998-12-12");
        model.setFullName("FirstName LastName");
        model.setPhoneNumber("+37477469582");

        User entity = new User();
        entity = userConverter.convertToEntity(model,entity);

        assertEquals(model.getAddress(),entity.getAddress());
        assertNull(entity.getEmail());
        assertEquals(model.getPhoneNumber(),entity.getPhoneNumber());
        assertNull(entity.getRole());
        assertEquals(model.getFullName(),entity.getFullName());
        assertEquals(model.getBirthDate(),entity.getBirthDate());
        assertNull(entity.getPassword());
    }
    @Test
    void testConvertToEntityWithEntityWithNullFields() {
        UserJson model = new UserJson();
        model.setEmail("admin@gmail.com");
        model.setPassword("admin12345");
        model.setRole("ROLE_ADMIN");

        User entity = new User();
        entity.setId(1L);
        entity.setEmail("storeAdmin@gmail.com");
        entity.setPassword("storeAdmin12345");
        entity.setRole(User.Role.ROLE_STORE_ADMIN);
        entity.setAddress("Address 45");
        entity.setBirthDate(LocalDate.of(1998, Month.AUGUST,8));
        entity.setFullName("FirstName LastName");
        entity.setPhoneNumber("+37477469582");

        entity = userConverter.convertToEntity(model,entity);

        assertEquals("Address 45",entity.getAddress());
        assertEquals("storeAdmin@gmail.com",entity.getEmail());
        assertEquals("+37477469582",entity.getPhoneNumber());
        assertEquals(User.Role.ROLE_STORE_ADMIN,entity.getRole());
        assertEquals("FirstName LastName",entity.getFullName());
        assertEquals(LocalDate.of(1998, Month.AUGUST,8),entity.getBirthDate());
        assertEquals("storeAdmin12345",entity.getPassword());
    }
    @Test
    void testConvertToEntityWithoutEntity() {
        UserJson model = new UserJson();
        model.setEmail("admin@gmail.com");
        model.setPassword("admin12345");
        model.setRole("ROLE_ADMIN");
        model.setAddress("Address 45");
        model.setBirthDate("1998-12-12");
        model.setFullName("FirstName LastName");
        model.setPhoneNumber("+37477469582");

        User entity = userConverter.convertToEntity(model);

        assertEquals(model.getAddress(),entity.getAddress());
        assertEquals(model.getEmail(),entity.getEmail());
        assertEquals(model.getPhoneNumber(),entity.getPhoneNumber());
        assertEquals(model.getRole(),entity.getRole());
        assertEquals(model.getFullName(),entity.getFullName());
        assertEquals(model.getBirthDate(),entity.getBirthDate());
        assertNull(entity.getPassword());
    }

    @Test
    void convertToModel() {
        User entity = new User();
        entity.setId(1L);
        entity.setEmail("admin@gmail.com");
        entity.setPassword("admin12345");
        entity.setRole(User.Role.ROLE_ADMIN);
        entity.setAddress("Address 45");
        entity.setBirthDate(LocalDate.of(1998, Month.AUGUST,8));
        entity.setFullName("FirstName LastName");
        entity.setPhoneNumber("+37477469582");

        UserJson model = userConverter.convertToModel(entity);

        assertEquals(entity.getId(),model.getId());
        assertEquals(entity.getAddress(),model.getAddress());
        assertEquals(entity.getEmail(),model.getEmail());
        assertEquals(entity.getPhoneNumber(),model.getPhoneNumber());
        assertEquals(entity.getRole(),model.getRole());
        assertEquals(entity.getFullName(),model.getFullName());
        assertEquals(entity.getBirthDate(),model.getBirthDate());
        assertNull(model.getPassword());
    }

    @Test
    void convertToListModel() {
        User entity = new User();
        entity.setId(1L);
        entity.setEmail("admin@gmail.com");
        entity.setPassword("admin12345");
        entity.setRole(User.Role.ROLE_ADMIN);
        entity.setAddress("Address 45");
        entity.setBirthDate(LocalDate.of(1998, Month.AUGUST,8));
        entity.setFullName("FirstName LastName");
        entity.setPhoneNumber("+37477469582");

        User entityTwo = new User();
        entityTwo.setId(1L);
        entityTwo.setEmail("storeAdmin@gmail.com");
        entityTwo.setPassword("storeAdmin12345");
        entityTwo.setRole(User.Role.ROLE_STORE_ADMIN);
        entityTwo.setAddress("Address 45");
        entityTwo.setBirthDate(LocalDate.of(1998, Month.AUGUST,8));
        entityTwo.setFullName("FirstName LastName");
        entityTwo.setPhoneNumber("+37477469582");

        Page<User> usersPage = new PageImpl<>(Arrays.asList(entity,entityTwo));
        UserListJson userListJson = userConverter.convertToListModel(usersPage);
        assertEquals(usersPage.getNumberOfElements(),userListJson.getTotalCount());

        UserJson model = userListJson.getUserListJson().get(0);
        UserJson modelTwo = userListJson.getUserListJson().get(1);

        assertEquals(entity.getId(),model.getId());
        assertEquals(entity.getAddress(),model.getAddress());
        assertEquals(entity.getEmail(),model.getEmail());
        assertEquals(entity.getPhoneNumber(),model.getPhoneNumber());
        assertEquals(entity.getRole(),model.getRole());
        assertEquals(entity.getFullName(),model.getFullName());
        assertEquals(entity.getBirthDate(),model.getBirthDate());
        assertNull(model.getPassword());

        assertEquals(entityTwo.getId(),modelTwo.getId());
        assertEquals(entityTwo.getAddress(),modelTwo.getAddress());
        assertEquals(entityTwo.getEmail(),modelTwo.getEmail());
        assertEquals(entityTwo.getPhoneNumber(),modelTwo.getPhoneNumber());
        assertEquals(entityTwo.getRole(),modelTwo.getRole());
        assertEquals(entityTwo.getFullName(),modelTwo.getFullName());
        assertEquals(entityTwo.getBirthDate(),modelTwo.getBirthDate());
        assertNull(modelTwo.getPassword());
    }
    @Test
    void convertToListModelFromEmptyPage() {
        Page<User> usersPage = new PageImpl<>(List.of());
        UserListJson userListJson = userConverter.convertToListModel(usersPage);
        assertEquals(0,userListJson.getTotalCount());
        assertTrue(userListJson.getUserListJson().isEmpty());
    }
}