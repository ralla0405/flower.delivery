package com.kirinit.service.flower.delivery.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "deliveryFee")
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
}
