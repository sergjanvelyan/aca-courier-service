package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.UserConverter;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.UserJson;
import com.aca.acacourierservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Autowired
    public UserService(UserRepository userRepository, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    public User getUserById(long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new CourierServiceException("There is no user with id" + id);
        }
        return userOptional.get();
    }

    @Transactional
    public UserJson saveUser(UserJson model) {
        User user = userConverter.convertToEntity(model);
        return userConverter.convertToModel(userRepository.save(user));
    }

    @Transactional
    public UserJson saveUser(User entity) {
        return userConverter.convertToModel(userRepository.save(entity));
    }

    @Transactional
    public void updateUser(UserJson model, long userId) {
        User entity = getUserById(userId);
        entity = userConverter.convertToEntity(model, entity);
        userRepository.save(entity);
    }

    @Transactional
    public void deleteUserById(long id) {
        if (!userRepository.existsById(id)) {
            throw new CourierServiceException("There is no user with id " + id);
        }
        userRepository.deleteById(id);
    }
}