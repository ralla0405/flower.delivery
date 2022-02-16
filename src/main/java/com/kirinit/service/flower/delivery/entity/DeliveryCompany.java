package com.kirinit.service.flower.delivery.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "delivery_company")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryCompany {

    @Id @GeneratedValue
    @Column(name = "delivery_company_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "deliveryCompany", fetch = FetchType.LAZY)
    private Delivery delivery;

    @OneToMany(mappedBy = "deliveryCompany", cascade = CascadeType.ALL)
    private List<DeliveryFee> deliveryFees = new ArrayList<>();
}
