package com.pedrobne.salesservice.controller;

import com.pedrobne.salesservice.dto.SaleRequestDTO;
import com.pedrobne.salesservice.dto.SaleResponseDTO;
import com.pedrobne.salesservice.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sale")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @PostMapping("/online")
    public ResponseEntity<SaleResponseDTO> online(
            @RequestHeader("X-User-Login") String login,
            @RequestBody SaleRequestDTO dto
    ) {
        SaleRequestDTO safeDto = new SaleRequestDTO(dto.cartId(), login);
        return ResponseEntity.ok(saleService.makeOnlineSale(safeDto));
    }

    @PostMapping("/physical")
    public ResponseEntity<SaleResponseDTO> physical(
            @RequestHeader("X-User-Login") String login,
            @RequestBody SaleRequestDTO dto
    ) {
        SaleRequestDTO safeDto = new SaleRequestDTO(dto.cartId(), login);
        return ResponseEntity.ok(saleService.makePhysicalSale(safeDto));
    }
}