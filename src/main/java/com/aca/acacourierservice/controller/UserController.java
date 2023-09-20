package com.aca.acacourierservice.controller;

import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.model.Status;
import com.aca.acacourierservice.model.UserJson;
import com.aca.acacourierservice.model.UserListJson;
import com.aca.acacourierservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE ,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Status> login(@RequestBody User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            return new ResponseEntity<>(new Status("Authentication failed"), HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(new Status("Login successful"));
    }
    //   /profile/get GET (Courier, storeAdmin, admin)
    @GetMapping(value="/profile/get")
    @Secured({"ROLE_ADMIN", "ROLE_STORE_ADMIN", "ROLE_COURIER"})
    public ResponseEntity<UserJson> getUserProfile(){
        //TODO:implement logic
        UserJson userJson = new UserJson();
        return new ResponseEntity<>(userJson,HttpStatus.OK);
    }
    //   /profile/update PUT ( Courier, storeAdmin, admin )
    @PutMapping(value ="/profile/update" )
    @Secured({"ROLE_ADMIN", "ROLE_STORE_ADMIN", "ROLE_COURIER"})
    public ResponseEntity<Status> updateUserProfile(@RequestBody UserJson userJson){
        //TODO:implement logic
        return new ResponseEntity<>(new Status("Profile updated"),HttpStatus.OK);
    }
    //   /courier/register POST (admin)
    @PostMapping("/courier/register")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Status> register(@RequestBody UserJson userJson) {
        userService.saveUser(userJson);
        return new ResponseEntity<>(new Status("Registered"),HttpStatus.OK);
    }
    //   /courier/{courierId} GET (admin)
    @GetMapping(value="/courier/{courierId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserJson> getCourier(@PathVariable String courierId){
        //TODO:implement logic
        UserJson userJson = new UserJson();
        return new ResponseEntity<>(userJson,HttpStatus.OK);
    }
    //   /courier/list GET (admin)
    @GetMapping(value="/courier/list")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserListJson> getCourierList(){
        //TODO:implement logic
        UserListJson userListJson = new UserListJson();
        return new ResponseEntity<>(userListJson,HttpStatus.OK);
    }
    //   /courier/update PUT (admin)
    @PutMapping(value ="/courier/{courierId}")
    public ResponseEntity<Status> updateCourier(@RequestBody UserJson userJson,@PathVariable long courierId){
        //TODO:implement logic
        return new ResponseEntity<>(new Status("Courier updated"),HttpStatus.OK);
    }
    //   /courier/{delete} DELETE (admin)
    @DeleteMapping(value ="/courier/{courierId}")
    public ResponseEntity<Status> deleteCourier(@PathVariable long courierId){
        //TODO:implement logic
        return new ResponseEntity<>(new Status("Courier updated"),HttpStatus.OK);
    }
}