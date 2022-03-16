package com.kirinit.service.flower.delivery.repository;

import com.kirinit.service.flower.delivery.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long>, DeliveryCustomRepository {
}
