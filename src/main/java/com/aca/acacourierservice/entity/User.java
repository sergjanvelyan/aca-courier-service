package com.aca.acacourierservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    @Email(message = "Invalid email")
    private String email;
    @Column(nullable = false)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+{}\\[\\]:;<>,.?~\\\\-]{8,}$", message = "Invalid password")
    private String password;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column
    private String address;
    @Column
    @Pattern(regexp = "^\\+?\\d+$", message = "Invalid phone number")
    private String phoneNumber;
    @Column
    @Pattern(regexp = "^[A-Za-z]+(?:[ A-Za-z'-]*[A-Za-z]+)*$", message = "Invalid name")
    private String fullName;
    @Column
    @Temporal(TemporalType.DATE)
    private LocalDate birthDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
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
        return this.birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public enum Role {
        ROLE_ADMIN,
        ROLE_STORE_ADMIN,
        ROLE_COURIER
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id)
                && Objects.equals(email, user.email)
                && Objects.equals(password, user.password)
                && role == user.role
                && Objects.equals(address, user.address)
                && Objects.equals(phoneNumber, user.phoneNumber)
                && Objects.equals(fullName, user.fullName)
                && Objects.equals(birthDate, user.birthDate);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id,
                email,
                password,
                role,
                address,
                phoneNumber,
                fullName,
                birthDate);
    }
}