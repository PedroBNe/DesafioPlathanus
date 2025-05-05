package com.pedrobne.salesservice.dto;

public record SaleResponseDTO(
        Long id,
        Long vehicleId,
        String clientLogin,
        String sellerLogin,
        String type
) {
}
