package com.kirinit.service.flower.delivery.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "delivery_fee")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryFee {

    @Id
    @GeneratedValue
    @Column(name = "deliveryfee_id")
    private Long id;

    @Column(name = "area_name")
    private String areaName;

    @Column(name = "price")
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_company_id")
    private DeliveryCompany deliveryCompany;

    public void setDeliveryCompany(DeliveryCompany deliveryCompany) {
        this.deliveryCompany = deliveryCompany;
    }
}
