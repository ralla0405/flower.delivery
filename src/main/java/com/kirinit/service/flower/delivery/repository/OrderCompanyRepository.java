package com.kirinit.service.flower.delivery.repository;

import com.kirinit.service.flower.delivery.entity.OrderCompany;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderCompanyRepository extends JpaRepository<OrderCompany, Long> {
    boolean existsByName(String name);
}
