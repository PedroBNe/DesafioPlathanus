package com.pedrobne.authservice.dto;

public record UserResponseDTO(
        String cpf,
        String name,
        String login,
        String role
) {
}
