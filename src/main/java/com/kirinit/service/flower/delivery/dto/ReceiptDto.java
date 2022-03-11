package com.kirinit.service.flower.delivery.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReceiptDto {

    private int no;
    private String date;
    private String time;
    private String itemName;
    private String toTel;
    private String memo;
    private String address;
    private String orderCompanyTel;
}
