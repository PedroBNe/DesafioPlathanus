package com.pedrobne.vehicleservice.dto;

import com.pedrobne.vehicleservice.model.Vehicle;

import java.math.BigDecimal;

public record VehicleDetailDTO(
        Long id,
        String model,
        Integer manufacturingYear,
        BigDecimal basePrice,
        Vehicle.Color color,
        boolean available,
        boolean pcd,
        boolean pessoaJuridica
) {
}
