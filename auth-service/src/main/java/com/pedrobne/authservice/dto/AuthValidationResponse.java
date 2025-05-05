package com.pedrobne.authservice.dto;

public record AuthValidationResponse(
        String login,
        String name,
        String role,
        boolean authenticated
) {}
