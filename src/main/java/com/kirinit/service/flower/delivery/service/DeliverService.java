package com.kirinit.service.flower.delivery.service;

import com.kirinit.service.flower.delivery.entity.Delivery;
import com.kirinit.service.flower.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliverService {

    private final DeliveryRepository deliveryRepository;

    /**
     * 배달 조회
     */
    public List<Delivery> findDeliveries(String start, String end) {
        return deliveryRepository.findAllByDateBetween(start, end);
    }

    /**
     * 배달 저장
     */
    @Transactional
    public Long insert(Delivery delivery) {
        System.out.println("delivery ========= " + delivery.getCreatedBy());
        System.out.println("delivery = " + delivery.getLastModifiedDate());
        Delivery save = deliveryRepository.save(delivery);
        return delivery.getId();
    }
}
