package com.pedrobne.apigateway.dto;

public record AuthValidationResponse(
        String login,
        String name,
        String role,
        boolean authenticated
) {
}
