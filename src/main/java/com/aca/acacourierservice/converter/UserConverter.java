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
        if(model.getAddress()!=null){
            entity.setAddress(model.getAddress());
        }
        if(model.getBirthDate()!=null){
            entity.setBirthDate(model.getBirthDate());
        }
        if(model.getFullName()!=null){
            entity.setFullName(model.getFullName());
        }
        if(model.getPhoneNumber()!=null){
            entity.setPhoneNumber(model.getPhoneNumber());
        }
        return entity;
    }
    @Override
    public User convertToEntity(UserJson model) {
        User entity = new User();
        entity.setEmail(model.getEmail());
        entity.setRole(model.getRole());
        entity.setAddress(model.getAddress());
        entity.setBirthDate(model.getBirthDate());
        entity.setFullName(model.getFullName());
        entity.setPhoneNumber(model.getPhoneNumber());
        return entity;
    }
    @Override
    public UserJson convertToModel(User entity) {
        UserJson model = new UserJson();
        model.setId(entity.getId());
        model.setEmail(entity.getEmail());
        model.setRole(entity.getRole());
        model.setAddress(entity.getAddress());
        model.setBirthDate(entity.getBirthDate());
        model.setFullName(entity.getFullName());
        model.setPhoneNumber(entity.getPhoneNumber());
        return model;
    }
    public UserListJson convertToListModel(Page<User> users){
        UserListJson userListJson = new UserListJson();
        userListJson.setTotalCount(users.getTotalElements());
        List<User> userList = users.getContent();
        List<UserJson> usersJson = new ArrayList<>();
        for (User user:userList){
            UserJson userJson = convertToModel(user);
            usersJson.add(userJson);
        }
        userListJson.setUserListJson(usersJson);
        return userListJson;
    }
}