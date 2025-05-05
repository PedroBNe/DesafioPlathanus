package com.pedrobne.salesservice.repository;

import com.pedrobne.salesservice.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}
