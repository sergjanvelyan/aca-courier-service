package com.aca.acacourierservice.model;

import com.aca.acacourierservice.entity.User;

public class UserJson {
    private String email;
    private String password;
    private User.Role role;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User.Role getRole() {
        return role;
    }

    public void setRole(User.Role role) {
        this.role = role;
    }
}
