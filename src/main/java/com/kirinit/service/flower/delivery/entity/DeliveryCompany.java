package com.kirinit.service.flower.delivery.entity;

import com.kirinit.service.flower.delivery.entity.audit.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "delivery_company")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryCompany extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "delivery_company_id")
    private Long id;

    @OneToOne(mappedBy = "deliveryCompany", fetch = FetchType.LAZY)
    private Delivery delivery;

    @Column(name = "name")
    private String name;

    //===비즈니스 로직===//
    /**
     * 데이터 변경
     */
    public void change(String name) {
        this.name = name;
    }
}
