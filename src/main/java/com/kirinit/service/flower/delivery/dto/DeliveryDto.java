package com.kirinit.service.flower.delivery.dto;

import lombok.Data;

@Data
public class DeliveryDto {

    private Long id;
    private String date;
    private String time;
    private String address;
    private String toTel;
    private String toName;
    private String itemName;
    private String memo;
    private String orderCompanyName;
    private String orderCompanyTel;
    private String deliveryCompanyName;
    private String color;
    private int price;
    private String dispatchNo;
}
