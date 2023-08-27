package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.model.UserJson;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter<User, UserJson>{
    @Override
    public User convertToEntity(UserJson model, User entity) {
        entity.setEmail(model.getEmail());
        entity.setRole(model.getRole());
        entity.setPassword(model.getPassword());
        entity.setAddress(model.getAddress());
        entity.setBirthdate(model.getBirthDate());
        entity.setFullName(model.getFullName());
        entity.setPhoneNumber(model.getPhoneNumber());
        return entity;
    }
    @Override
    public User convertToEntity(UserJson model) {
        User entity = new User();
        return convertToEntity(model,entity);
    }
    @Override
    public UserJson convertToModel(User entity) {
        UserJson model = new UserJson();
        model.setEmail(entity.getEmail());
        model.setRole(entity.getRole());
        model.setPassword(entity.getPassword());
        model.setAddress(entity.getAddress());
        model.setBirthDate(entity.getBirthdate());
        model.setFullName(entity.getFullName());
        model.setPhoneNumber(entity.getPhoneNumber());
        return model;
    }
}