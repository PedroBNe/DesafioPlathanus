package com.pedrobne.vehicleservice.controller;

import com.pedrobne.vehicleservice.dto.VehicleDetailDTO;
import com.pedrobne.vehicleservice.dto.VehicleResponseDTO;
import com.pedrobne.vehicleservice.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
    @RequestMapping("/vehicle")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @GetMapping("/list")
    public ResponseEntity<List<VehicleResponseDTO>> listAvailableVehicles() {
        return ResponseEntity.ok(vehicleService.getAllAvailableVehicles());
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<VehicleDetailDTO> getVehicleDetail(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleDetail(id));
    }

    @PutMapping("/availability/{id}")
    public ResponseEntity<Void> updateAvailability(@PathVariable Long id, @RequestParam boolean available) {
        vehicleService.setAvailability(id, available);
        return ResponseEntity.ok().build();
    }
}
