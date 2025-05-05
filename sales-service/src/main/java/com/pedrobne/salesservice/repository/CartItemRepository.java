package com.pedrobne.salesservice.repository;

import com.pedrobne.salesservice.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{
    Optional<CartItem> findByVehicleIdAndConfirmedFalse(Long vehicleId);
    List<CartItem> findByUsernameAndConfirmedFalse(String username);

}
