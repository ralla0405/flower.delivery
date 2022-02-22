package com.kirinit.service.flower.delivery.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter @Setter
public class DeliveryDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime date;
    private String time;
    private String address;
    private String toTel;
    private String toName;
    private String itemName;
    private String memo;
    private String orderCompanyName;
    private String orderCompanyTel;
    private String deliveryCompanyName;
    private String price;
    private String dispatchNo;
}
