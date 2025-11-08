package com.bookticket.user_service.dto;

public record JwtResponse(
        String token,
        String type,
        long expiresIn
) {
    public JwtResponse(String token, long expiresIn) {
        this(token, "Bearer", expiresIn);
    }
}
