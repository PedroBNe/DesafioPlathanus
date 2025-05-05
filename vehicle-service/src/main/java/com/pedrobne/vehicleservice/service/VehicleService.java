package com.pedrobne.vehicleservice.service;

import com.pedrobne.vehicleservice.dto.VehicleDetailDTO;
import com.pedrobne.vehicleservice.dto.VehicleResponseDTO;
import com.pedrobne.vehicleservice.model.Vehicle;
import com.pedrobne.vehicleservice.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public List<VehicleResponseDTO> getAllAvailableVehicles() {
        return vehicleRepository.findByAvailableTrue().stream()
                .map(vehicle -> new VehicleResponseDTO(
                        vehicle.getId(),
                        vehicle.getModel(),
                        vehicle.getManufacturingYear(),
                        vehicle.getBasePrice(),
                        vehicle.getColor(),
                        vehicle.isAvailable()
                ))
                .collect(Collectors.toList());
    }

    public VehicleDetailDTO getVehicleDetail(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        BigDecimal finalPrice = vehicle.getBasePrice()
                .add(vehicle.getBasePrice().multiply(BigDecimal.valueOf(vehicle.getColor().getMultiplier())));

        return new VehicleDetailDTO(
                vehicle.getId(),
                vehicle.getModel(),
                vehicle.getManufacturingYear(),
                finalPrice,
                vehicle.getColor(),
                vehicle.isAvailable(),
                vehicle.isPcd(),
                vehicle.isPessoaJuridica()

        );
    }

    public void setAvailability(Long id, boolean available) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        vehicle.setAvailable(available);
        vehicleRepository.save(vehicle);
    }
}   