package com.kirinit.service.flower.delivery;

import com.kirinit.service.flower.delivery.entity.Authority;
import com.kirinit.service.flower.delivery.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        private final PasswordEncoder passwordEncoder;

        public void dbInit() {
            System.out.println("Init = " + this.getClass());

            Authority authority1 = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

            Authority authority2 = Authority.builder()
                .authorityName("ROLE_ADMIN")
                .build();

            Set<Authority> authorities1 = new HashSet<>();
            authorities1.add(authority1);
            authorities1.add(authority2);

            Member member = Member.builder()
                .userId("admin")
                .username("adminName")
                .password(passwordEncoder.encode("admin"))
                .authorities(authorities1)
                .build();

            em.persist(member);
        }
    }
}
