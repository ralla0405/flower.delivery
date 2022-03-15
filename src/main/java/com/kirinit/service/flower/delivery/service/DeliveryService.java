package com.kirinit.service.flower.delivery.service;

import com.kirinit.service.flower.delivery.entity.Delivery;
import com.kirinit.service.flower.delivery.entity.DeliverySearch;
import com.kirinit.service.flower.delivery.entity.DeliveryStatus;
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

    /**
     * 배달 전체 조회
     */
    public List<Delivery> findDeliveries(DeliverySearch deliverySearch) {
        return deliveryRepository.findAll(deliverySearch);
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
    public void delivery(Delivery delivery) {
        deliveryRepository.save(delivery);
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
                               String deliveryCompanyName,
                               int price,
                               String dispatchNo) {
        Optional<Delivery> findDelivery = deliveryRepository.findById(deliveryId);
        findDelivery.get().change(no, date, time, address, toTel, toName, itemName, memo, orderCompanyName, orderCompanyTel, price, dispatchNo, deliveryCompanyName);
    }

    /**
     * 배송 상태 수정
     */
    @Transactional
    public void updateStatus(Long deliveryId, DeliveryStatus deliveryStatus) {
        Optional<Delivery> findDelivery = deliveryRepository.findById(deliveryId);
        findDelivery.get().changeStatus(deliveryStatus);
    }

    /**
     * 배송 색상 수정
     */
    @Transactional
    public void updateColor(Long deliveryId, String color) {
        Optional<Delivery> findDelivery = deliveryRepository.findById(deliveryId);
        findDelivery.get().changeColor(color);
    }
    /**
     * 배달 삭제
     */
    @Transactional
    public void deleteDelivery(Long deliveryId) {
        deliveryRepository.deleteById(deliveryId);
    }
}
