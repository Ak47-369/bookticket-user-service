package com.bookticket.user_service.controller;

import com.bookticket.user_service.dto.*;
import com.bookticket.user_service.exception.ResourceNotFoundException;
import com.bookticket.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CreateUserRequest createUserRequest) {
        return ResponseEntity.ok(userService.createUser(createUserRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return null;
//        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @GetMapping("/me")
    public ResponseEntity<UserSummary> getUserByUsername(@AuthenticationPrincipal UserDetails userDetails) {
        String userName = userDetails.getUsername();
        return ResponseEntity.ok(userService.getUserByUsername(userName));
    }

    @PutMapping("/update")
    public ResponseEntity<UserSummary> updateUserByUsername(@AuthenticationPrincipal UserDetails userDetails,@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        String userName = userDetails.getUsername();
        return ResponseEntity.ok(userService.updateUserByUsername(userName, updateUserRequest));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUserByUsername(@AuthenticationPrincipal UserDetails userDetails) {
        String userName = userDetails.getUsername();
        userService.deleteUserByUsername(userName);
        return ResponseEntity.noContent().build();
    }
}
