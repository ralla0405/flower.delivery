package com.kirinit.service.flower.delivery.service;

import com.kirinit.service.flower.delivery.entity.DeliveryCompany;
import com.kirinit.service.flower.delivery.entity.DeliveryFee;
import com.kirinit.service.flower.delivery.repository.DeliveryCompanyRepository;
import com.kirinit.service.flower.delivery.repository.DeliveryFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryFeeService {

    private final DeliveryCompanyRepository deliveryCompanyRepository;
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

    /**
     * 배송비 수정
     */
    @Transactional
    public void updateDeliveryFee(Long deliveryFeeId, Long deliveryCompanyId, String areaName, int price) {
        Optional<DeliveryFee> findDeliveryFee = deliveryFeeRepository.findById(deliveryFeeId);
        Optional<DeliveryCompany> findDeliveryCompany = deliveryCompanyRepository.findById(deliveryCompanyId);

        findDeliveryFee.get().change(findDeliveryCompany.get(), areaName, price);
    }
}
