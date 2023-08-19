package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.model.StoreJson;
import com.aca.acacourierservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StoreConverter implements Converter<Store, StoreJson> {
    private UserRepository userRepository;

    @Autowired
    public StoreConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Store convertToEntity(StoreJson model, Store entity) {
        entity.setName(model.getName());
        //After writing UserService, change this part with getUser() method
        if (model.getAdminId() != 0) {
            Optional<User> userOptional = userRepository.findById(model.getAdminId());
            if (userOptional.isEmpty()) {
                throw new RuntimeException("There is no admin with id " + model.getAdminId());
            }
            entity.setAdmin(userOptional.get());
        }
        entity.setPickupPoints(model.getPickupPoints());

        return entity;
    }

    @Override
    public Store convertToEntity(StoreJson model) {
        return null;
    }

    @Override
    public StoreJson convertToModel(Store entity, StoreJson model) {
        model.setName(entity.getName());
        model.setAdminId(entity.getAdmin().getId());
        model.setPickupPoints(entity.getPickupPoints());

        return model;
    }
}
