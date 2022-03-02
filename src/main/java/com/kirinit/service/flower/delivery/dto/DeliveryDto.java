package com.kirinit.service.flower.delivery.dto;

import com.kirinit.service.flower.delivery.entity.DeliveryCompany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter
@ToString(of = {"date", "time", "address", "toTel", "toName", "itemName", "memo", "orderCompanyName", "orderCompanyTel", "deliveryCompanyName", "price", "dispatchNo"})
public class DeliveryDto {

    private String date;
    private String time;
    private String address;
    private String toTel;
    private String toName;
    private String itemName;
    private String memo;
    private String orderCompanyName;
    private String orderCompanyTel;
    private DeliveryCompany deliveryCompany;
    private String price;
    private String dispatchNo;
}
