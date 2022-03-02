package com.kirinit.service.flower.delivery.service;

import com.kirinit.service.flower.delivery.entity.DeliveryFee;
import com.kirinit.service.flower.delivery.repository.DeliveryFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryFeeService {

    private final DeliveryFeeRepository deliveryFeeRepository;

    /**
     * 배송비 전체 조회
     */
    public List<DeliveryFee> findDeliveryFees() {
        return deliveryFeeRepository.findAll();
    }

    /**
     * 배송비 등록
     */
    @Transactional
    public void DeliverFee(List<DeliveryFee> deliveryFees) {
        deliveryFeeRepository.saveAll(deliveryFees);
    }
}
