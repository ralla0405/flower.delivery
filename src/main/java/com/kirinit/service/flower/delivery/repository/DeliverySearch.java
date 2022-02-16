package com.kirinit.service.flower.delivery.repository;

import com.kirinit.service.flower.delivery.entity.DeliveryStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DeliverySearch {
    
    private String MemberId; // 회원 아이디
    private DeliveryStatus deliveryStatus; // 배송상태 [READY, COM, CHECK]
}
