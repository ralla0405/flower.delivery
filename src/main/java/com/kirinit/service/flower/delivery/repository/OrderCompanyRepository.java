package com.kirinit.service.flower.delivery.repository;

import com.kirinit.service.flower.delivery.entity.OrderCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderCompanyRepository extends JpaRepository<OrderCompany, Long> {
    boolean existsByName(String name);
    Optional<OrderCompany> findByName(String name);
}
