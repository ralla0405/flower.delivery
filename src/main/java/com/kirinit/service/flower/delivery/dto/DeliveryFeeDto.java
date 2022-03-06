package com.kirinit.service.flower.delivery.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeliveryFeeDto {

    private Long id;

    private DeliveryCompanyDto deliveryCompanyDto;

    @NotNull
    private String areaName;

    @NotNull
    private int price;
}
