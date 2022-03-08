package com.kirinit.service.flower.delivery.service;

import com.kirinit.service.flower.delivery.dto.OrderCompanyDto;
import com.kirinit.service.flower.delivery.entity.OrderCompany;
import com.kirinit.service.flower.delivery.repository.OrderCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderCompanyService {

    private final OrderCompanyRepository orderCompanyRepository;

    /**
     * 발주업체 전체 조회
     */
    public List<OrderCompany> findOrderCompanies() {
        return orderCompanyRepository.findAll();
    }

    /**
     * 발주업체 단일 조회 (업체명으로 조회)
     */
    public Optional<OrderCompany> findByName(String name) {
        return orderCompanyRepository.findByName(name);
    }

    /**
     * 발주업체 중복 검토
     */
    public boolean validateDuplicateOrderCompany(OrderCompanyDto orderCompanyDto) {
        boolean isExisted = orderCompanyRepository.existsByName(orderCompanyDto.getName());
        if (isExisted) {
            if (orderCompanyDto.getId() != null) {
                return !orderCompanyRepository.findById(orderCompanyDto.getId()).get().getName().equals(orderCompanyDto.getName());
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 발주업체 등록
     */
    @Transactional
    public void OrderCompany(List<OrderCompany> orderCompanies) {
        orderCompanyRepository.saveAll(orderCompanies);
    }

    /**
     * 발주업체 수정
     */
    @Transactional
    public void updateOrderCompany(Long orderCompanyId, String name, String tel) {
        Optional<OrderCompany> findOrderCompany = orderCompanyRepository.findById(orderCompanyId);
        findOrderCompany.get().change(name, tel);
    }

    /**
     * 발주업체 삭제
     */
    @Transactional
    public void deleteOrderCompany(Long orderCompanyId) {
        orderCompanyRepository.deleteById(orderCompanyId);
    }
}
