package com.kirinit.service.flower.delivery.repository;

import com.kirinit.service.flower.delivery.entity.Delivery;
import com.kirinit.service.flower.delivery.entity.DeliverySearch;

import java.util.List;

public interface DeliveryCustomRepository {

    List<Delivery> findAll(DeliverySearch deliverySearch);
}
