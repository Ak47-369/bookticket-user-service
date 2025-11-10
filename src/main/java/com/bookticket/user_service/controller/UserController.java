package com.bookticket.user_service.controller;

import com.bookticket.user_service.dto.*;
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

    @GetMapping("/me")
    public ResponseEntity<UserSummary> getUserByUsername(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PutMapping("/update")
    public ResponseEntity<JwtResponse> updateUserByUsername(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        String email = userDetails.getUsername();
        JwtResponse jwtResponse = userService.updateUserByEmail(email, updateUserRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUserByEmail(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        userService.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }
}
