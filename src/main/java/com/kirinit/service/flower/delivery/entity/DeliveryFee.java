package com.kirinit.service.flower.delivery.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_company_id")
    private DeliveryCompany deliveryCompany;

    @Column(name = "area_name", unique = true)
    private String areaName;

    @Column(name = "price")
    private int price;

    //===비즈니스 로직===//
    /**
     * 데이터 변경
     */
    public void change(DeliveryCompany deliveryCompany, String areaName, int price) {
        this.deliveryCompany = deliveryCompany;
        this.areaName = areaName;
        this.price = price;
    }
}
