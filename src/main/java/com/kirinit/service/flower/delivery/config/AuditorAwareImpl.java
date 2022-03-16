package com.kirinit.service.flower.delivery.config;

import com.kirinit.service.flower.delivery.util.SecurityUtil;
import org.springframework.data.domain.AuditorAware;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return SecurityUtil.getCurrentUsername();
    }
}
