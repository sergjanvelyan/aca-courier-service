package com.aca.acacourierservice.controller;

import com.aca.acacourierservice.converter.UserConverter;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.*;
import com.aca.acacourierservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;
    private final UserConverter userConverter;
    private final AuthenticationManager authenticationManager;
    @Autowired
    public UserController(UserService userService, UserConverter userConverter, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.userConverter = userConverter;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE ,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Status> login(@RequestBody UserJson userJson) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userJson.getEmail(), userJson.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new ResponseEntity<>(new Status("You are successfully logged in:"),HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new Status("You have entered an invalid username or password:"), HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping(value="/profile/get")
    @Secured({"ROLE_ADMIN", "ROLE_STORE_ADMIN", "ROLE_COURIER"})
    public ResponseEntity<?> getUserProfile(){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            UserJson userJson = userConverter.convertToModel(userService.getUserByEmail(username));
            userJson.setPassword("Password hidden");
            return new ResponseEntity<>(userJson,HttpStatus.OK);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(new Status("Not found:"),HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping(value ="/profile/update" )
    @Secured({"ROLE_ADMIN", "ROLE_STORE_ADMIN", "ROLE_COURIER"})
    public ResponseEntity<?> updateUserProfile(@RequestBody UserJson userJson){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            userService.updateUser(userJson,username);
            return new ResponseEntity<>(new Status("Profile updated:"),HttpStatus.OK);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(new Status("Not found:"),HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/courier/register")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Status> register(@RequestBody UserJson userJson) {
        userJson.setRole(User.Role.ROLE_COURIER);
        try {
            long id = userService.saveUser(userJson).getId();
            return new ResponseEntity<>(new StatusWithId("Registered:", id),HttpStatus.OK);
        } catch (CourierServiceException e) {
            return new ResponseEntity<>(new Status(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value="/courier/{courierId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> getCourier(@PathVariable Long courierId){
        try{
            UserJson userJson = userConverter.convertToModel(userService.getUserById(courierId));
            userJson.setPassword("Password hidden");
            if(!userJson.getRole().equals(User.Role.ROLE_COURIER)){
                throw new CourierServiceException();
            }
            return new ResponseEntity<>(userJson,HttpStatus.OK);
        } catch (CourierServiceException e){
            return new ResponseEntity<>(new StatusWithId("There is no courier", courierId),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value="/courier/list")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> getCourierList(@RequestBody PageInfo pageInfo){
        Page<User> couriers = userService.getCouriers(pageInfo.getPage(), pageInfo.getCount());
        if(couriers.isEmpty()){
            return new ResponseEntity<>(new Status("There are no couriers:"), HttpStatus.NO_CONTENT);
        }
        UserListJson userListJson = userConverter.convertToListModel(couriers);
        return new ResponseEntity<>(userListJson,HttpStatus.OK);
    }
    @PutMapping(value ="/courier/{courierId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> updateCourier(@RequestBody UserJson courierJson,@PathVariable long courierId){
        try{
            User courier = userService.getUserById(courierId);
            if (!courier.getRole().equals(User.Role.ROLE_COURIER)){
                throw new CourierServiceException();
            }
            courierJson.setRole(User.Role.ROLE_COURIER);
            userService.updateUser(courierJson,courier);
            return new ResponseEntity<>(new Status("Courier updated:"),HttpStatus.OK);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(new StatusWithId("There is no courier", courierId),HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            return new ResponseEntity<>(new Status(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping(value ="/courier/{courierId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> deleteCourier(@PathVariable long courierId){
        try {
            if(!userService.getUserById(courierId).getRole().equals(User.Role.ROLE_COURIER)){
                throw new CourierServiceException();
            }
            userService.deleteExistingUserById(courierId);
            return new ResponseEntity<>(new Status("Courier deleted"),HttpStatus.OK);
        }catch (CourierServiceException e){
            return new ResponseEntity<>(new StatusWithId("There is no courier", courierId),HttpStatus.BAD_REQUEST);
        }
    }
}