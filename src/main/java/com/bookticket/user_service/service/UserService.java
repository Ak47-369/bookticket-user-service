package com.bookticket.user_service.service;

import com.bookticket.user_service.dto.*;
import com.bookticket.user_service.entity.User;
import com.bookticket.user_service.enums.UserRole;
import com.bookticket.user_service.exception.ResourceNotFoundException;
import com.bookticket.user_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public String createUser(CreateUserRequest createUserRequest) {
        try {
            User user = new User();
            user.setUsername(createUserRequest.username());
            user.setEmail(createUserRequest.email());
            user.setPassword(createUserRequest.password());
//            user.setPassword(passwordEncoder.encode(createUserRequest.password()));
            user.setRoles(Set.of(UserRole.USER));
            User savedUser = userRepository.save(user);
            log.info("User created Successfully: {}", user.getUsername());
            // TO DO - Generate JWT token
//            return new LoginResponse(user);
            return "User Created Successfully" + savedUser;
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public LoginResponse loginUser(LoginRequest loginRequest) {
        return null;
    }

    public UserSummary getUserByUsername(String username){
        log.info("Getting user by username: {}", username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return UserSummary.builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(UserRole::name).toList())
                .build();
    }

    @Transactional
    public UserSummary updateUserByUsername(String username, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User ", "username", username));

        if(updateUserRequest.username() != null && !user.getUsername().equals(updateUserRequest.username()) ){
            user.setUsername(updateUserRequest.username());
        }

        if(updateUserRequest.email() != null && !user.getEmail().equals(updateUserRequest.email()) ){
            user.setEmail(updateUserRequest.email());
        }

        User savedUser = null;
        try {
            savedUser = userRepository.save(user);
            log.info("User updated Successfully: {}", user.getUsername());
        } catch (Exception e) {
            log.error("Error updating user: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return UserSummary.builder()
                .id(savedUser.getId().toString())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .roles(savedUser.getRoles().stream().map(UserRole::name).toList())
                .build();
    }

    public void deleteUserByUsername(String username) {
        Long id = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username)).getId();
        userRepository.deleteById(id);
        log.info("User deleted Successfully: {}", username);
    }
}
