package com.kirinit.service.flower.delivery.repository;

import com.kirinit.service.flower.delivery.entity.DeliveryStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter @Setter
public class DeliverySearch {
    
    private String MemberId; // 회원 아이디
    private DeliveryStatus deliveryStatus; // 배송상태 [READY, COM, CHECK]
    private LocalDateTime startDate; // 시작일
    private LocalDateTime endDate; // 끝일

    public String getFormatStartDate() {
        return this.startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getFormatEndDate() {
        return this.endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
