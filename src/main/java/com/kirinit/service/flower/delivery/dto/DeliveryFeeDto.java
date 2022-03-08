package com.kirinit.service.flower.delivery.dto;

import lombok.Data;

@Data
public class DeliveryFeeDto {

    private Long id;

    private DeliveryCompanyDto deliveryCompanyDto;

    private String areaName;

    private int price;
}
