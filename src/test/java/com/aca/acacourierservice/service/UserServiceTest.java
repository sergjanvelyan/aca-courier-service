package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.UserConverter;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.UserJson;
import com.aca.acacourierservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserConverter userConverter;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;
    @Test
    void testGetUserById() {
        long userId = 1;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        User result = userService.getUserById(userId);
        verify(userRepository,times(1)).findById(userId);
        assertEquals(user,result);
    }
    @Test
    void testGetUserByInvalidId() {
        long invalidId = 1;
        when(userRepository.findById(invalidId)).thenReturn(Optional.empty());
        CourierServiceException exception = assertThrows(CourierServiceException.class, () ->
                userService.getUserById(invalidId));
        assertEquals("There is no user with id=" + invalidId + ":",exception.getMessage());
    }
    @Test
    void testGetUserByEmail() {
        String email = "user@gmail.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        User result = userService.getUserByEmail(email);
        verify(userRepository,times(1)).findByEmail(email);
        assertEquals(user,result);
    }
    @Test
    void testGetUserByInvalidEmail() {
        String invalidEmail = "invalid@gmail.com";
        when(userRepository.findByEmail(invalidEmail)).thenReturn(Optional.empty());
        CourierServiceException exception = assertThrows(CourierServiceException.class, () ->
                userService.getUserByEmail(invalidEmail));
        assertEquals("There is no user with email '" + invalidEmail + "':",exception.getMessage());
    }
    @Test
    void testSaveUser() {
        UserJson userJson = new UserJson();
        userJson.setEmail("user@gmail.com");
        userJson.setPassword("password12345");
        when(userRepository.existsByEmail(userJson.getEmail())).thenReturn(false);

        User newUser = new User();
        when(userConverter.convertToEntity(userJson)).thenReturn(newUser);
        when(passwordEncoder.encode(userJson.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(newUser)).thenReturn(newUser);
        User result = userService.saveUser(userJson);
        verify(userConverter,times(1)).convertToEntity(userJson);
        verify(passwordEncoder,times(1)).encode(userJson.getPassword());
        verify(userRepository,times(1)).save(newUser);
        assertEquals(newUser, result);
    }
    @Test
    void testSaveUserWithExistingEmail() {
        UserJson userJson = new UserJson();
        userJson.setEmail("user@gmail.com");
        when(userRepository.existsByEmail(userJson.getEmail())).thenReturn(true);
        CourierServiceException exception = assertThrows(CourierServiceException.class, () ->
                userService.saveUser(userJson));
        assertEquals("This email is already in use",exception.getMessage());
    }
    @Test
    void testUpdateUserWithoutPasswordChange() throws CourierServiceException {
        String userEmail = "user@gmail.com";
        UserJson userJson = new UserJson();
        userJson.setEmail(userEmail);
        User existingUser = new User();
        existingUser.setEmail(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(existingUser));
        when(userConverter.convertToEntity(userJson, existingUser)).thenReturn(existingUser);
        userService.updateUser(userJson,userEmail);
        verify(userConverter,times(1)).convertToEntity(userJson,existingUser);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, times(1)).save(existingUser);
    }
    @Test
    void testUpdateUserWithPasswordChange() throws CourierServiceException {
        String userEmail = "user@gmail.com";
        String updatedPassword = "password12345";
        UserJson userJson = new UserJson();
        userJson.setEmail(userEmail);
        userJson.setPassword(updatedPassword);
        User existingUser = new User();
        existingUser.setEmail(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(existingUser));
        when(userConverter.convertToEntity(userJson, existingUser)).thenReturn(existingUser);
        when(passwordEncoder.encode(updatedPassword)).thenReturn("encodedPassword");
        userService.updateUser(userJson,userEmail);
        verify(userConverter,times(1)).convertToEntity(userJson,existingUser);
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).save(existingUser);
    }
    @Test
    void testUpdateUserWithInvalidEmail() {
        String invalidEmail = "invalid@gmail.com";
        UserJson userJson = new UserJson();
        userJson.setEmail(invalidEmail);
        when(userRepository.findByEmail(invalidEmail)).thenReturn(Optional.empty());
        CourierServiceException exception = assertThrows(CourierServiceException.class, () ->
                userService.updateUser(userJson, invalidEmail));
        assertEquals("There is no user with email '" + invalidEmail + "':", exception.getMessage());
    }
    @Test
    void testUpdateExistingUserWithPasswordChange() {
        String updatedPassword = "password12345";
        UserJson userJson = new UserJson();
        userJson.setPassword(updatedPassword);
        User existingUser = new User();
        when(userConverter.convertToEntity(userJson, existingUser)).thenReturn(existingUser);
        when(passwordEncoder.encode(updatedPassword)).thenReturn("encodedPassword");
        userService.updateUser(userJson,existingUser);
        verify(userConverter,times(1)).convertToEntity(userJson,existingUser);
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).save(existingUser);
    }
    @Test
    void testUpdateExistingUserWithoutPasswordChange() {
        UserJson userJson = new UserJson();
        User existingUser = new User();
        when(userConverter.convertToEntity(userJson, existingUser)).thenReturn(existingUser);
        userService.updateUser(userJson,existingUser);
        verify(userConverter,times(1)).convertToEntity(userJson,existingUser);
        verify(passwordEncoder,never()).encode(anyString());
        verify(userRepository, times(1)).save(existingUser);
    }
    @Test
    void testDeleteExistingUserById() {
        long userId = 1;
        assertDoesNotThrow(() -> userService.deleteExistingUserById(userId));
        verify(userRepository, times(1)).deleteById(userId);
    }
    @Test
    void testGetCouriers() {
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page,size);
        User courier = new User();
        courier.setRole(User.Role.ROLE_COURIER);
        List<User> courierList = List.of(courier);
        Page<User> courierPage = new PageImpl<>(courierList);
        when(userRepository.findAllByRole(User.Role.ROLE_COURIER, pageRequest)).thenReturn(courierPage);
        Page<User> result = userService.getCouriers(page, size);
        verify(userRepository,times(1)).findAllByRole(User.Role.ROLE_COURIER,pageRequest);
        assertEquals(courierPage, result);
    }
}