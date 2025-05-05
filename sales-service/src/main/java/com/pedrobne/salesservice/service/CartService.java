package com.pedrobne.salesservice.service;

import com.pedrobne.salesservice.dto.CartRequestDTO;
import com.pedrobne.salesservice.dto.CartResponseDTO;
import com.pedrobne.salesservice.model.CartItem;
import com.pedrobne.salesservice.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartItemRepository repository;
    private final WebClient.Builder webClientBuilder;

    private final String VEHICLE_SERVICE_URL = "http://vehicle-service";
    private final CartItemRepository cartItemRepository;

    public List<CartResponseDTO> getActiveCartByUsername(String username) {
        List<CartItem> items = cartItemRepository.findByUsernameAndConfirmedFalse(username);

        List<CartItem> validItems = items.stream()
                .filter(item -> item.getExpiration().isAfter(LocalDateTime.now()))
                .toList();

        items.stream()
                .filter(item -> item.getExpiration().isBefore(LocalDateTime.now()))
                .forEach(cartItemRepository::delete);

        if (validItems.isEmpty()) {
            throw new RuntimeException("Carrinho não encontrado ou expirado");
        }

        return validItems.stream()
                .map(item -> CartResponseDTO.builder()
                        .id(item.getId())
                        .vehicleId(item.getVehicleId())
                        .username(item.getUsername())
                        .expiration(item.getExpiration())
                        .confirmed(item.isConfirmed())
                        .build())
                .toList();
    }

    public CartResponseDTO addToCart(CartRequestDTO request) {
        if (request.vehicleId() == null || request.username() == null || request.username().isBlank()) {
            throw new IllegalArgumentException("vehicleId e username são obrigatórios");
        }
        repository.findByVehicleIdAndConfirmedFalse(request.vehicleId()).ifPresent(item -> {
            if (item.getExpiration().isAfter(LocalDateTime.now())) {
                throw new RuntimeException("Vehicle already in cart");
            }
            repository.delete(item);
            System.out.println("Atualizando disponibilidade do veículo: " + request.vehicleId() + " para disponível: " + request.username());
            updateAvailability(item.getVehicleId(), true);
        });

        // Marcar como indisponível
        updateAvailability(request.vehicleId(), false);

        CartItem cart = CartItem.builder()
                .vehicleId(request.vehicleId())
                .username(request.username())
                .expiration(LocalDateTime.now().plusMinutes(1))
                .confirmed(false)
                .build();

        CartItem saved = repository.save(cart);

        return new CartResponseDTO(
                saved.getId(),
                saved.getVehicleId(),
                saved.getUsername(),
                saved.getExpiration(),
                saved.isConfirmed()
        );
    }

    public void cancelCart(Long cartId) {
        CartItem item = repository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        repository.delete(item);
        updateAvailability(item.getVehicleId(), true);
    }

    public void confirmCart(Long cartId) {
        CartItem item = repository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (item.getExpiration().isBefore(LocalDateTime.now())) {
            updateAvailability(item.getVehicleId(), true);
            throw new RuntimeException("Cart expired");
        }

        item.setConfirmed(true);
        repository.save(item);
    }

    private void updateAvailability(Long vehicleId, boolean available) {
        webClientBuilder.build()
                .put()
                .uri(VEHICLE_SERVICE_URL + "/vehicle/availability/" + vehicleId + "?available=" + available)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}