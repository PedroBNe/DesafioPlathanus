package com.pedrobne.authservice.dto;

public record LoginRequestDTO(
        String login,
        String password
) {
}
