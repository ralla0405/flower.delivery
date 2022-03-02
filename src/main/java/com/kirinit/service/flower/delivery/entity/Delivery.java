package com.kirinit.service.flower.delivery.entity;

import com.kirinit.service.flower.delivery.entity.audit.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "delivery")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"no", "date", "time", "address", "deliveryCompanyName", "price",
        "itemName", "toName", "toTel", "memo", "orderCompanyName",
        "orderCompanyTel", "dispatchNo", "status"})
public class Delivery extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "no")
    private int no;

    @Column(name = "date")
    private String date;

    @Column(name = "time")
    private String time;

    @Column(name = "address")
    private String address;

    @Column(name = "to_tel")
    private String toTel;

    @Column(name = "to_name")
    private String toName;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "memo")
    private String memo;

    @Column(name = "odrer_company_name")
    private String orderCompanyName;

    @Column(name = "order_company_tel")
    private String orderCompanyTel;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_company")
    private DeliveryCompany deliveryCompany;

    @Column(name = "price")
    private String price;

    @Column(name = "dispatch_no")
    private String dispatchNo;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

}
