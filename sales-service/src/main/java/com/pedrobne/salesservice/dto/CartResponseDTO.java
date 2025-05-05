package com.pedrobne.salesservice.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CartResponseDTO(
        Long id,
        Long vehicleId,
        String username,
        LocalDateTime expiration,
        boolean confirmed
) {
}
