package com.kirinit.service.flower.delivery.service;

import com.kirinit.service.flower.delivery.dto.DeliveryFeeDto;
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
     * 배송비 전체 조회 (배송업체 조건)
     */
    public List<DeliveryFee> findDeliveryFeesByDeliveryCompany(DeliveryCompany deliveryCompany) {
        return deliveryFeeRepository.findDeliveryFeesByDeliveryCompany(deliveryCompany);
    }

    /**
     * 지역명 중복 검토
     * 중복이면 true 반환
     */
    public boolean validateDuplicateAreaName(DeliveryFeeDto deliveryFeeDto) {
        // 구역명 중복 확인
        Optional<DeliveryCompany> findDeliveryCompany = deliveryCompanyRepository.findById(deliveryFeeDto.getDeliveryCompanyDto().getId());
        boolean isExisted = deliveryFeeRepository.existsByDeliveryCompanyAndAreaName(findDeliveryCompany.get(), deliveryFeeDto.getAreaName());
        if (isExisted) {
            // 수정할 경우
            if (deliveryFeeDto.getId() != null) {
                DeliveryFee findDeliveryFee = deliveryFeeRepository.findById(deliveryFeeDto.getId()).get();
                return !(findDeliveryFee.getAreaName().equals(deliveryFeeDto.getAreaName()) && findDeliveryFee.getDeliveryCompany().getName().equals(deliveryFeeDto.getDeliveryCompanyDto().getName()));
            }
            return true;
        } else {
            return false;
        }
    }
    /**
     * 배송비 등록
     */
    @Transactional
    public void deliverFee(List<DeliveryFee> deliveryFees) {
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

    /**
     * 배송비 삭제
     */
    @Transactional
    public void deleteDeliveryFee(Long deliveryFeeId) {
        deliveryFeeRepository.deleteById(deliveryFeeId);
    }
}
