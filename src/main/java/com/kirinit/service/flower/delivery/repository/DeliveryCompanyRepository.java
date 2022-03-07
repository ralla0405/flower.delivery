package com.kirinit.service.flower.delivery.repository;

import com.kirinit.service.flower.delivery.entity.DeliveryCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryCompanyRepository extends JpaRepository<DeliveryCompany, Long> {

    boolean existsByName(String name);
}
