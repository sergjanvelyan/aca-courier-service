package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.UserConverter;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.UserJson;
import com.aca.acacourierservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
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
    public User getUserById(@Min(1) long id) throws CourierServiceException {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new CourierServiceException("There is no user with id="+id+":");
        }
        return userOptional.get();
    }
    public User getUserByEmail(@Email String email) throws CourierServiceException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new CourierServiceException("There is no user with email '"+email+"':");
        }
        return userOptional.get();
    }
    @Transactional
    public User saveUser(@Valid UserJson model) throws CourierServiceException{
        if(userRepository.existsByEmail(model.getEmail())) {
            throw new CourierServiceException("This email is already in use");
        }
        User user = userConverter.convertToEntity(model);
        user.setPassword(passwordEncoder.encode(model.getPassword()));
        return userRepository.save(user);
    }
    @Transactional
    public void saveUser(User entity) {
        if(userRepository.existsByEmail(entity.getEmail())) {
            throw new CourierServiceException("This email is already in use");
        }
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        userRepository.save(entity);
    }
    @Transactional
    public void updateUser(@Valid UserJson model, @Email String email) throws CourierServiceException {
        User entity = getUserByEmail(email);
        User.Role role = entity.getRole();

        entity = userConverter.convertToEntity(model, entity);
        entity.setPassword(passwordEncoder.encode(model.getPassword()));
        entity.setRole(role);
        entity.setEmail(email);
        userRepository.save(entity);
    }
    @Transactional
    public void updateUser(@Valid UserJson model, User entity) {
        User.Role role = entity.getRole();
        String email = entity.getEmail();

        entity = userConverter.convertToEntity(model, entity);
        entity.setPassword(passwordEncoder.encode(model.getPassword()));
        entity.setRole(role);
        entity.setEmail(email);
        userRepository.save(entity);
    }
    @Transactional
    public void deleteExistingUserById(@Min(1) long id) {
        userRepository.deleteById(id);
    }
    public Page<User> getCouriers(int page,int size){
        return userRepository.findAllByRole(User.Role.ROLE_COURIER, PageRequest.of(page,size));
    }
}