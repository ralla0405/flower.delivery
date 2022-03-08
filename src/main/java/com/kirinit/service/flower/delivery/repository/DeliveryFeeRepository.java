package com.kirinit.service.flower.delivery.repository;

import com.kirinit.service.flower.delivery.entity.DeliveryCompany;
import com.kirinit.service.flower.delivery.entity.DeliveryFee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DeliveryFeeRepository extends JpaRepository<DeliveryFee, Long> {

    // 배송업체, 구역명으로 존재하는지 여부
    boolean existsByDeliveryCompanyAndAreaName(DeliveryCompany deliveryCompany, String areaName);

    // 배송업체로 배송비 전체 조회
    List<DeliveryFee> findDeliveryFeesByDeliveryCompany(DeliveryCompany deliveryCompany);
}
