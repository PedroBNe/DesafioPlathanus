package com.pedrobne.vehicleservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;

    private Integer manufacturingYear;

    @Column(name = "base_price", nullable = false)
    private BigDecimal basePrice;

    @Enumerated(EnumType.STRING)
    private Color color;

    @Column(name = "available", nullable = true)
    private boolean available;

    @Column(name = "pcd", nullable = true)
    private boolean pcd;

    private boolean pessoaJuridica;

    public enum Color {
        BRANCA(0),
        PRATA(0.01),
        PRETA(0.02);

        private final double multiplier;

        Color(double multiplier) {
            this.multiplier = multiplier;
        }

        public double getMultiplier() {
            return multiplier;
        }
    }
}
