package com.pedrobne.authservice.dto;

public record AuthenticatedUserDTO(
        String login,
        String name,
        String token,
        Boolean isAdmin,
        Boolean authenticated
) {
}
