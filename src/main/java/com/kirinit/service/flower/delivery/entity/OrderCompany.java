package com.kirinit.service.flower.delivery.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "OrderCompany")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCompany {
    @Id
    @GeneratedValue
    @Column(name = "ordercompany_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "tel")
    private String tel;

    @OneToOne(mappedBy = "orderCompany", fetch = FetchType.LAZY)
    private Delivery delivery;

}
