package com.bookticket.user_service.dto;

import java.util.List;

public record UserSummary(
        String id,
        String username,
        String email,
        List<String> roles
) {
}
