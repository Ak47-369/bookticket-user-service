package com.bookticket.user_service.dto;

public record LoginResponse(
        JwtResponse jwtResponse,
        UserSummary userSummary
) {
}
