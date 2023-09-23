package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.model.UserJson;
import com.aca.acacourierservice.model.UserListJson;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
    public UserListJson convertToListModel(Page<User> users){
        UserListJson userListJson = new UserListJson();
        userListJson.setTotalCount(users.getTotalElements());
        List<UserJson> usersJson = new ArrayList<>();
        for (User user:users){
            usersJson.add(convertToModel(user));
        }
        userListJson.setUserListJson(usersJson);
        return userListJson;
    }
}