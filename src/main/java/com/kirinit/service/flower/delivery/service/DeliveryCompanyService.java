package com.kirinit.service.flower.delivery.service;

import com.kirinit.service.flower.delivery.entity.DeliveryCompany;
import com.kirinit.service.flower.delivery.repository.DeliveryCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryCompanyService {

    private final DeliveryCompanyRepository deliveryCompanyRepository;

    /**
     * 배송업체 전체 조회
     */
    public List<DeliveryCompany> findDeliveryCompanies() {
        return deliveryCompanyRepository.findAll();
    }

    /**
     * 배송업체 단일 조회
     */
    public Optional<DeliveryCompany> findOne(Long deliveryCompanyId) {
        return deliveryCompanyRepository.findById(deliveryCompanyId);
    }

    /**
     * 배송업체 등록
     */
    @Transactional
    public void DeliveryCompany(List<DeliveryCompany> deliveryCompanies) {
        deliveryCompanyRepository.saveAll(deliveryCompanies);
    }

    /**
     * 배송업체 수정
     */
    @Transactional
    public void updateDeliveryCompany(Long deliveryCompanyId, String name) {
        Optional<DeliveryCompany> findDeliveryCompany = deliveryCompanyRepository.findById(deliveryCompanyId);
        findDeliveryCompany.get().change(name);
    }
}