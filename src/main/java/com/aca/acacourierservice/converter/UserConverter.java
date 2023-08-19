package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.model.UserJson;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter<User, UserJson>{

    @Override
    public User convertToEntity(UserJson model, User entity) {
        return null;
    }

    @Override
    public User convertToEntity(UserJson model) {
        return null;
    }

    @Override
    public UserJson convertToModel(User entity, UserJson model) {
        return null;
    }
}
