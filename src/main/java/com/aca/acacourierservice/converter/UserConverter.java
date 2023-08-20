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
        return entity;
    }

    @Override
    public User convertToEntity(UserJson model) {
        User entity = new User();
        entity.setEmail(model.getEmail());
        entity.setRole(model.getRole());
        entity.setPassword(model.getPassword());
        return entity;
    }

    @Override
    public UserJson convertToModel(User entity) {
        UserJson model = new UserJson();
        model.setEmail(entity.getEmail());
        model.setRole(entity.getRole());
        model.setPassword(entity.getPassword());
        return model;
    }
}
