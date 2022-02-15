package com.kirinit.service.flower.delivery.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "delivery")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @Column(name = "no")
    private int no;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "time")
    private String time;

    @Column(name = "address")
    private String address;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "deliverycompany_id")
    private DeliveryCompany deliveryCompany;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "to_name")
    private String toName;

    @Column(name = "to_tel")
    private String toTel;

    @Column(name = "memo")
    private String memo;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ordercompany_id")
    private OrderCompany orderCompany;

    @Column(name = "dispatch_no")
    private String dispatchNo;

    @Column(name = "status")
    private DeliveryStatus status;

}
