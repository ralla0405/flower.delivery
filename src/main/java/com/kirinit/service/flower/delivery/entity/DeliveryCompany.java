package com.kirinit.service.flower.delivery.entity;

import com.kirinit.service.flower.delivery.entity.audit.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "color")
    private String color;

    @OneToMany(mappedBy = "deliveryCompany", cascade = CascadeType.ALL)
    private List<DeliveryFee> deliveryFees;

    //===비즈니스 로직===//
    /**
     * 데이터 변경
     */
    public void change(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
