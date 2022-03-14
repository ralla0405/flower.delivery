package com.kirinit.service.flower.delivery.dto;

import lombok.Data;

@Data
public class DeliverySearchDto {

    private String deliveryStatus;
    private String startDate;
    private String endDate;
}
