package com.kirinit.service.flower.delivery.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kirinit.service.flower.delivery.entity.audit.BaseEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    @Column(name = "delivery_company_name")
    private String deliveryCompanyName;

    @Column(name = "price")
    private String price;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "to_name")
    private String toName;

    @Column(name = "to_tel")
    private String toTel;

    @Column(name = "memo")
    private String memo;

    @Column(name = "odrer_company_name")
    private String orderCompanyName;

    @Column(name = "order_company_tel")
    private String orderCompanyTel;

    @Column(name = "dispatch_no")
    private String dispatchNo;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    public void setMember(Member member) {
        this.member = member;
    }

}
