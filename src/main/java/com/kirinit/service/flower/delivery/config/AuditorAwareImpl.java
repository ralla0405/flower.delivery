package com.kirinit.service.flower.delivery.config;

import com.kirinit.service.flower.delivery.entity.Member;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Member> {

    @Override
    public Optional<Member> getCurrentAuditor() {

        // spring security 로그인 정보에서 ID를 받아 return
        return Optional.empty();
    }
}
