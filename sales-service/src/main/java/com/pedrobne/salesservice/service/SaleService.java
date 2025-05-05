package com.pedrobne.salesservice.service;

import com.pedrobne.salesservice.dto.SaleRequestDTO;
import com.pedrobne.salesservice.dto.SaleResponseDTO;
import com.pedrobne.salesservice.model.CartItem;
import com.pedrobne.salesservice.model.Sale;
import com.pedrobne.salesservice.repository.CartItemRepository;
import com.pedrobne.salesservice.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final CartItemRepository cartItemRepository;

    public SaleResponseDTO makeOnlineSale(SaleRequestDTO dto) {
        return makeSale(dto, Sale.Type.ONLINE);
    }

    public SaleResponseDTO makePhysicalSale(SaleRequestDTO dto) {
        return makeSale(dto, Sale.Type.PHYSICAL);
    }

    private SaleResponseDTO makeSale(SaleRequestDTO dto, Sale.Type type) {
        CartItem cart = cartItemRepository.findById(dto.cartId())
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

        if (cart.getExpiration().isBefore(java.time.LocalDateTime.now())) {
            throw new RuntimeException("Carrinho expirado");
        }

        cart.setConfirmed(true);
        cartItemRepository.save(cart);

        Sale sale = Sale.builder()
                .vehicleId(cart.getVehicleId())
                .clientLogin(type == Sale.Type.ONLINE ? dto.login() : null)
                .sellerLogin(type == Sale.Type.PHYSICAL ? dto.login() : null)
                .type(type)
                .build();

        Sale saved = saleRepository.save(sale);

        return new SaleResponseDTO(
                saved.getId(),
                saved.getVehicleId(),
                saved.getClientLogin(),
                saved.getSellerLogin(),
                saved.getType().name()
        );
    }
}