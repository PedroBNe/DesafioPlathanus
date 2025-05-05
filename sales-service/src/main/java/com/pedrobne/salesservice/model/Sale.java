package com.pedrobne.salesservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long vehicleId;

    private String clientLogin;

    private String sellerLogin;

    @Enumerated(EnumType.STRING)
    private Type type;

    public enum Type {
        ONLINE, PHYSICAL
    }
}
