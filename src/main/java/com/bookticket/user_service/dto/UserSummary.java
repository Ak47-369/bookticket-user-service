package com.bookticket.user_service.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record UserSummary(
        String id,
        String username,
        String email,
        List<String> roles
) {
}
