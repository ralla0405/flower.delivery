package com.kirinit.service.flower.delivery.config;

import com.kirinit.service.flower.delivery.entity.Member;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaAuditConfig {

    @Bean
    public AuditorAware<Member> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
