package com.aca.acacourierservice.model;

import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.validation.OnUpdate;
import com.aca.acacourierservice.view.Lists;
import com.aca.acacourierservice.view.PrivateFirstLevel;
import com.aca.acacourierservice.view.PrivateSecondLevel;
import com.aca.acacourierservice.view.Public;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class UserJson {
    @JsonView(Lists.class)
    private long id;
    @JsonView(Public.class)
    @Email(message = "The email ${validatedValue} is not valid")
    @Null(groups = OnUpdate.class, message = "You can't change the email")
    private String email;
    @JsonView(PrivateFirstLevel.class)
    @NotNull
    @Size(min = 8, message = "Password should be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$", message = "Password should contain at least 1 letter and 1 number")
    private String password;
    @JsonView(PrivateSecondLevel.class)
    @Null(groups = OnUpdate.class, message = "You can't change the role")
    private User.Role role;
    @JsonView(Public.class)
    @Pattern(regexp = "^(([a-zA-Z]{2,15}\\s?)+|[1-9][0-9]?+)\\s([1-9][0-9]?+[a-zA-Z]?|([1-9][0-9]?+/[1-9][0-9]?+))$",message = "Not valid address")
    private String address;
    @JsonView(Public.class)
    @Pattern(regexp = "^[+][0-9]{10,15}$", message = "Invalid phone number")
    private String phoneNumber;
    @JsonView(Public.class)
    @Pattern(regexp = "^(([a-zA-Z]{2,15})\\s?)+$", message = "Name should contain letters")
    private String fullName;
    @JsonView(Public.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}