package com.kirinit.service.flower.delivery.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
public class DeliverySearch {

    private DeliveryStatus deliveryStatus; // 배송상태 [READY, COM, CHECK]

    private String startDate; // 시작일

    private String endDate; // 끝일
}
