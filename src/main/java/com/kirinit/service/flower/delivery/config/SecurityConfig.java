package com.kirinit.service.flower.delivery.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration // IoC 빈(Bean)을 등록
@EnableWebSecurity // 필터 체인 관리 시작 어노테이션
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .antMatchers(
                        "/favicon.ico"
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
            .antMatchers("/deliveries/**").authenticated()
            .antMatchers("/deliveryCompanies/**").access("hasRole('ROLE_ADMIN')")
            .antMatchers("/deliveryFees/**").access("hasRole('ROLE_ADMIN')")
            .antMatchers("/orderCompanies/**").access("hasRole('ROLE_ADMIN')")
            .antMatchers("/members/**").access("hasRole('ROLE_ADMIN')")
            .anyRequest().permitAll()

        .and()
            .formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/loginProc")
            .defaultSuccessUrl("/deliveries");
    }
}
