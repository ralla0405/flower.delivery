package com.kirinit.service.flower.delivery.repository;

import com.kirinit.service.flower.delivery.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findAllByDateBetween(String start, String end);


}
