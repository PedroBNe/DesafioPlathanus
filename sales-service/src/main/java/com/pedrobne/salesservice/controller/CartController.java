package com.pedrobne.salesservice.controller;

import com.pedrobne.salesservice.dto.CartRequestDTO;
import com.pedrobne.salesservice.dto.CartResponseDTO;
import com.pedrobne.salesservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/user/{username}")
    public ResponseEntity<List<CartResponseDTO>> getActiveCartByUser(@PathVariable String username) {
        return ResponseEntity.ok(cartService.getActiveCartByUsername(username));
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponseDTO> addToCart(@RequestBody CartRequestDTO request) {
        return ResponseEntity.ok(cartService.addToCart(request));
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        cartService.cancelCart(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm/{id}")
    public ResponseEntity<Void> confirm(@PathVariable Long id) {
        cartService.confirmCart(id);
        return ResponseEntity.ok().build();
    }
}
