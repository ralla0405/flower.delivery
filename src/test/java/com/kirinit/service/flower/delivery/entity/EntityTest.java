package com.kirinit.service.flower.delivery.entity;

import com.kirinit.service.flower.delivery.service.DeliveryService;
import com.kirinit.service.flower.delivery.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SpringBootTest
@Transactional
public class EntityTest {
    @Autowired EntityManager em;
    @Autowired private MemberService memberService;
    @Autowired private DeliveryService deliveryService;

    @Test
    public void member() throws Exception {
        //given

        //when
        Member member = Member.builder()
                .username("admin")
                .name("username")
                .password("admin")
                .role(MemberRole.ROLE_ADMIN)
                .build();

        em.persist(member);

        em.flush();
        em.clear();

        //then
        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();
        for (Member member1 : members) {
            System.out.println("member1.getUsername() = " + member1.getUsername());
            System.out.println("member1.getName() = " + member1.getName());
            System.out.println("member1.getPassword() = " + member1.getPassword());
        }

    }

    @Test
    @Rollback(value = false)
    public void delivery() throws Exception {
        //given

        Optional<Member> member = memberService.findMember(6L);

        //when
//        Delivery delivery = Delivery.builder()
//                .member(member.get())
//                .no(1)
//                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
//                .time("16:12")
//                .address("경기도 광주시 신현리 968-2 302호")
//                .deliveryCompanyName("시노")
//                .price("10000")
//                .itemName("율마")
//                .toName("너에게")
//                .toTel("02-0000-0000")
//                .memo("메모해주세요.")
//                .orderCompanyName("ㅏ아니")
//                .orderCompanyTel("02-2099-984")
//                .dispatchNo("2")
//                .status(DeliveryStatus.READY)
//                .build();
        Delivery delivery = Delivery.builder()
                .member(member.get())
                .no(1)
                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .address("경기도 광주시 신현리 968-2 302호")
                .status(DeliveryStatus.READY)
                .build();
        deliveryService.delivery(delivery);

        //then
        // 한번에 조회 하는 방법 찾아야함


    }
}
