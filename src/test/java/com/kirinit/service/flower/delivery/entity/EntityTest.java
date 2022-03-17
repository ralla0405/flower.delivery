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


    }

    @Test
    public void delivery() throws Exception {
        //given

        Optional<Member> member = memberService.findMember(6L);

        //when

        //then
        // 한번에 조회 하는 방법 찾아야함


    }
}
