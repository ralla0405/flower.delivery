package com.kirinit.service.flower.delivery.service;

import com.kirinit.service.flower.delivery.entity.Member;
import com.kirinit.service.flower.delivery.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findOneWithAuthoritiesByUsername(username)
                .map(member -> createUser(username, member))
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> DB 에서 찾을 수 없습니다."));
    }

    private org.springframework.security.core.userdetails.User createUser(String userId, Member member) {
//        if (!member.isActivated()) {
//            throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
//        }
        List<GrantedAuthority> grantedAuthorities = member.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(member.getUsername(),
                member.getPassword(),
                grantedAuthorities);
    }
}
