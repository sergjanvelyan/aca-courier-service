package com.aca.acacourierservice.model;

import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.validation.*;
import com.aca.acacourierservice.view.Lists;
import com.aca.acacourierservice.view.PrivateFirstLevel;
import com.aca.acacourierservice.view.PrivateSecondLevel;
import com.aca.acacourierservice.view.Public;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserJson {
    @JsonView(Lists.class)
    private long id;
    @JsonView(Public.class)
    @NotNull(groups = {OnCreate.class, OnLogin.class},  message = "Please enter email")
    @Email(message = "The email ${validatedValue} is not valid")
    @Null(groups = OnUpdate.class, message = "You can't change the email")
    private String email;
    @JsonView(PrivateFirstLevel.class)
    @NotNull(groups = {OnCreate.class, OnLogin.class},  message = "Please enter password")
    @Size(min = 8, message = "Password should be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$", message = "Password should contain at least 1 letter and 1 number")
    private String password;
    @JsonView(PrivateSecondLevel.class)
    @Null(groups = OnUpdate.class, message = "You can't change the role")
    @ValidEnum(enumClass = User.Role.class)
    private String role;
    @JsonView(PrivateSecondLevel.class)
    @NotNull(groups = OnCreate.class,  message = "Please enter address")
    @Pattern(regexp = "^(([a-zA-Z]{2,15}\\s?)+|[1-9][0-9]{0,5})\\s([1-9][0-9]{0,5}[a-zA-Z]?|([1-9][0-9]{0,5}/[1-9][0-9]{0,5}))$",message = "Not valid address")
    private String address;
    @JsonView(PrivateSecondLevel.class)
    @NotNull(groups = OnCreate.class,  message = "Please enter phone number")
    @Pattern(regexp = "^[+][0-9]{10,15}$", message = "Invalid phone number")
    private String phoneNumber;
    @JsonView(Public.class)
    @NotNull(groups = OnCreate.class,  message = "Please enter full name")
    @Pattern(regexp = "^(([a-zA-Z]{2,15})\\s?)+$", message = "Name should contain letters")
    private String fullName;
    @JsonView(PrivateSecondLevel.class)
    @NotNull(groups = OnCreate.class,  message = "Please enter your birth date")
    @ValidLocalDate(groups = {OnCreate.class, OnUpdate.class})
    private String birthDate;

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
        if(role==null||role.isEmpty()){
            return null;
        }
        return User.Role.valueOf(role.toUpperCase());
    }

    public void setRole(String role) {
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
        if (birthDate==null||birthDate.isEmpty()){
            return null;
        }
        try{
            return LocalDate.parse(birthDate);
        }catch (DateTimeParseException e){
            throw new IllegalArgumentException("Invalid date format: " + birthDate);
        }
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}