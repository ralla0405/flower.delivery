package com.kirinit.service.flower.delivery.service;

import com.kirinit.service.flower.delivery.entity.Delivery;
import com.kirinit.service.flower.delivery.entity.DeliveryCompany;
import com.kirinit.service.flower.delivery.repository.DeliveryCompanyRepository;
import com.kirinit.service.flower.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryCompanyRepository deliveryCompanyRepository;

    /**
     * 배달 전체 조회
     */
    public List<Delivery> findDeliveries(String start, String end) {
        return deliveryRepository.findAllByDateBetween(start, end);
    }

    /**
     * 배달 단일 조회
     */
    public Optional<Delivery> findOne(Long deliveryId) {
        return deliveryRepository.findById(deliveryId);
    }
    /**
     * 배달 저장
     */
    @Transactional
    public Long Delivery(Delivery delivery) {
        deliveryRepository.save(delivery);
        return delivery.getId();
    }

    /**
     * 배달 수정
     */
    @Transactional
    public void updateDelivery(Long deliveryId,
                               int no,
                               String date,
                               String time,
                               String address,
                               String toTel,
                               String toName,
                               String itemName,
                               String memo,
                               String orderCompanyName,
                               String orderCompanyTel,
                               Long deliveryCompanyId,
                               int price,
                               String dispatchNo) {
        Optional<Delivery> findDelivery = deliveryRepository.findById(deliveryId);
        Optional<DeliveryCompany> findDeliveryCompany = deliveryCompanyRepository.findById(deliveryCompanyId);
        findDelivery.get().change(no, date, time, address, toTel, toName, itemName, memo, orderCompanyName, orderCompanyTel, price, dispatchNo, findDeliveryCompany.get());
    }
    /**
     * 배달 삭제
     */
    @Transactional
    public void deleteDelivery(Long deliveryId) {
        deliveryRepository.deleteById(deliveryId);
    }
}
