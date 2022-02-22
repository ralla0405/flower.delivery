package com.kirinit.service.flower.delivery.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter @Setter
public class DeliverySearch {
    
    private String MemberId; // 회원 아이디
    private DeliveryStatus deliveryStatus; // 배송상태 [READY, COM, CHECK]

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startDate; // 시작일

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime endDate; // 끝일
}
