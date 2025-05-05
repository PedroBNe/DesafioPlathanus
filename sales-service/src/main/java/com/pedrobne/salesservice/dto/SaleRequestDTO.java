package com.pedrobne.salesservice.dto;

public record SaleRequestDTO(
        Long cartId,
        String login
) {
}
