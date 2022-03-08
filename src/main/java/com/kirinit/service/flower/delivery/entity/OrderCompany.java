package com.kirinit.service.flower.delivery.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "order_company")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCompany {
    @Id
    @GeneratedValue
    @Column(name = "order_company_id")
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "tel")
    private String tel;

    //===비즈니스 로직===//
    /**
     * 데이터 변경
     */
    public void change(String name, String tel) {
        this.name = name;
        this.tel = tel;
    }
}
