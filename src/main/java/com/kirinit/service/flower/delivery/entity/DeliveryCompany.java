package com.kirinit.service.flower.delivery.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "deliverycompany")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryCompany {

    @Id @GeneratedValue
    @Column(name = "deliverycompany_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "deliveryCompany", fetch = FetchType.LAZY)
    private Delivery delivery;
}
