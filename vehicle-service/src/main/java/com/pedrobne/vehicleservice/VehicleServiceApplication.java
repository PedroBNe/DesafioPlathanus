package com.pedrobne.vehicleservice;

import com.pedrobne.vehicleservice.model.Vehicle;
import com.pedrobne.vehicleservice.repository.VehicleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@SpringBootApplication
public class VehicleServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehicleServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataLoader(VehicleRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(Vehicle.builder()
                        .model("Onix LT 1.0")
                        .manufacturingYear(2022)
                        .basePrice(BigDecimal.valueOf(70000.00))
                        .color(Vehicle.Color.PRATA)
                        .available(true)
                        .build());

                repository.save(Vehicle.builder()
                        .model("HB20 Comfort")
                        .manufacturingYear(2021)
                        .basePrice(BigDecimal.valueOf(68000.00))
                        .color(Vehicle.Color.BRANCA)
                        .available(true)
                        .build());

                repository.save(Vehicle.builder()
                        .model("Fiat Argo Drive")
                        .manufacturingYear(2023)
                        .basePrice(BigDecimal.valueOf(75000.00))
                        .color(Vehicle.Color.PRETA)
                        .available(true)
                        .build());
            }
        };
    }

}
