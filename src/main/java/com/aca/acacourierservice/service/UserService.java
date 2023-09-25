package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.UserConverter;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.UserJson;
import com.aca.acacourierservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserConverter userConverter, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserById(long id) throws CourierServiceException {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new CourierServiceException("There is no user with id="+id+":");
        }
        return userOptional.get();
    }
    public User getUserByEmail(String email) throws CourierServiceException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new CourierServiceException("There is no user with email '"+email+"':");
        }
        return userOptional.get();
    }
    @Transactional
    public UserJson saveUser(UserJson model) {
        User user = userConverter.convertToEntity(model);
        user.setPassword(passwordEncoder.encode(model.getPassword()));
        return userConverter.convertToModel(userRepository.save(user));
    }

    @Transactional
    public UserJson saveUser(User entity) {
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        return userConverter.convertToModel(userRepository.save(entity));
    }

    @Transactional
    public void updateUser(UserJson model, long userId) throws CourierServiceException {
        User entity = getUserById(userId);
        entity.setPassword(passwordEncoder.encode(model.getPassword()));
        entity = userConverter.convertToEntity(model, entity);
        userRepository.save(entity);
    }
    @Transactional
    public void updateUser(UserJson model, String email) throws CourierServiceException {
        User entity = getUserByEmail(email);
        entity = userConverter.convertToEntity(model, entity);
        entity.setPassword(passwordEncoder.encode(model.getPassword()));
        userRepository.save(entity);
    }
    @Transactional
    public void updateUser(UserJson model, User entity) {
        entity = userConverter.convertToEntity(model, entity);
        entity.setPassword(passwordEncoder.encode(model.getPassword()));
        userRepository.save(entity);
    }

    @Transactional
    public void deleteUserById(long id) throws CourierServiceException {
        if (!userRepository.existsById(id)) {
            throw new CourierServiceException("There is no user with id="+id+":");
        }
        userRepository.deleteById(id);
    }
    @Transactional
    public void deleteExistingUserById(long id) {
        userRepository.deleteById(id);
    }
    public Page<User> getCouriers(int page,int size){
        return userRepository.findAllByRole(User.Role.ROLE_COURIER, PageRequest.of(page,size));
    }
}