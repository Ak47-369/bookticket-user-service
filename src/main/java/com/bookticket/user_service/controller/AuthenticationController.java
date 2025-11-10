package com.bookticket.user_service.controller;

import com.bookticket.user_service.dto.*;
import com.bookticket.user_service.service.CustomUserDetails;
import com.bookticket.user_service.service.UserDetailsServiceImpl;
import com.bookticket.user_service.service.UserService;
import com.bookticket.user_service.utils.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public AuthenticationController(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService,
                                    JwtUtils jwtUtils, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CreateUserRequest createUserRequest) {
        UserSummary userSummary = userService.createUser(createUserRequest);
        final CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(userSummary.email());
        final String jwt = jwtUtils.generateToken(userDetails);
        final long expiresIn = jwtUtils.extractExpiration(jwt).getTime();

        return ResponseEntity.ok(new LoginResponse(userSummary, new JwtResponse(jwt, expiresIn)));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );

        final CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(loginRequest.email());
        final String jwt = jwtUtils.generateToken(userDetails);
        final long expiresIn = jwtUtils.extractExpiration(jwt).getTime();

        return ResponseEntity.ok(new JwtResponse(jwt, expiresIn));
    }
}
