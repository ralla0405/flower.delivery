package com.kirinit.service.flower.delivery.entity;

import com.kirinit.service.flower.delivery.entity.audit.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "delviery_fee")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryFee extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "delivery_fee_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_company_id")
    private DeliveryCompany deliveryCompany;

    @Column(name = "area_name")
    private String areaName;

    @Column(name = "price")
    private int price;
}
