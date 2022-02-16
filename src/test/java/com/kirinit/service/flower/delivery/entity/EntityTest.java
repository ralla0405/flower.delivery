package com.kirinit.service.flower.delivery.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
@Transactional
public class EntityTest {
    @Autowired EntityManager em;

    @Test
    public void member() throws Exception {
        //given
        Authority userAuthority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();
        Authority adminAuthority = Authority.builder()
                .authorityName("ROLE_ADMIN")
                .build();
        Set<Authority> authoritiesSet = new HashSet<>();
        authoritiesSet.add(userAuthority);
        authoritiesSet.add(adminAuthority);

        em.persist(userAuthority);
        em.persist(adminAuthority);

        //when
        Member member = Member.builder()
                .userId("admin")
                .username("username")
                .password("admin")
                .authorities(authoritiesSet)
                .build();

        em.persist(member);

        em.flush();
        em.clear();

        //then
        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();
        for (Member member1 : members) {
            System.out.println("member1.getUserId() = " + member1.getUserId());
            System.out.println("member1.getUsername() = " + member1.getUsername());
            System.out.println("member1.getPassword() = " + member1.getPassword());
        }

    }

    @Rollback(value = false)
    @Test
    public void delivery() throws Exception {
        //given
        Authority userAuthority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();
        em.persist(userAuthority);

        Member member = Member.builder()
                .userId("user")
                .username("username")
                .password("user")
                .authorities(Collections.singleton(userAuthority))
                .build();
        em.persist(member);

        OrderCompany orderCompany = OrderCompany.builder()
                .name("시노플라워")
                .tel("02-0000-0000")
                .build();
        em.persist(orderCompany);

        DeliveryFee deliveryFee = DeliveryFee.builder()
                .areaName("광주시")
                .price(50000)
                .build();

        DeliveryCompany deliveryCompany = DeliveryCompany.builder()
                .name("개인기사")
                .deliveryFees(Collections.singletonList(deliveryFee))
                .build();
        em.persist(deliveryCompany);

        deliveryFee.setDeliveryCompany(deliveryCompany);

        em.persist(deliveryFee);
        
        //when
        Delivery delivery = Delivery.builder()
                .member(member)
                .no(1)
                .date(LocalDateTime.now())
                .time("16:12")
                .address("경기도 광주시 신현리 968-2 302호")
                .deliveryCompany(deliveryCompany)
                .itemName("율마")
                .toName("너에게")
                .toTel("02-0000-0000")
                .memo("메모해주세요.")
                .orderCompany(orderCompany)
                .dispatchNo("2")
                .status(DeliveryStatus.READY)
                .build();
        em.persist(delivery);
        
        em.flush();
        em.clear();

        //then
        // 한번에 조회 하는 방법 찾아야함
        List<Delivery> deliveries = em.createQuery("select d from Delivery d", Delivery.class)
                .getResultList();
        for (Delivery findDelivery : deliveries) {
            System.out.println("findDelivery.getNo() = " + findDelivery.getNo());
            System.out.println("findDelivery.getDate() = " + findDelivery.getDate());
            System.out.println("findDelivery.getTime() = " + findDelivery.getTime());
            System.out.println("findDelivery.getAddress() = " + findDelivery.getAddress());
            System.out.println("findDelivery.getDeliveryCompany().getName() = " + findDelivery.getDeliveryCompany().getName());
            for (DeliveryFee fee : findDelivery.getDeliveryCompany().getDeliveryFees()) {
                System.out.println("fee.getAreaName() = " + fee.getAreaName());
                System.out.println("fee.getPrice() = " + fee.getPrice());
            }
            System.out.println("findDelivery.getItemName() = " + findDelivery.getItemName());
            System.out.println("findDelivery.getToName() = " + findDelivery.getToName());
            System.out.println("findDelivery.getToTel() = " + findDelivery.getToTel());
            System.out.println("findDelivery.getMemo() = " + findDelivery.getMemo());
            System.out.println("findDelivery.getOrderCompany().getName() = " + findDelivery.getOrderCompany().getName());
            System.out.println("findDelivery.getOrderCompany().getTel() = " + findDelivery.getOrderCompany().getTel());
            System.out.println("findDelivery.getDispatchNo() = " + findDelivery.getDispatchNo());
            System.out.println("findDelivery.getStatus() = " + findDelivery.getStatus());
        }

    }
}
