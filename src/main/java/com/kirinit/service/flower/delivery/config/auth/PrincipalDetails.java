package com.kirinit.service.flower.delivery.config.auth;

import com.kirinit.service.flower.delivery.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@Data
public class PrincipalDetails implements UserDetails {

    private final Member member; // composition

    // 계정이 가지고있는 권한 목록을 리턴한다
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> member.getRole().name());
        return authorities;
    }

    // 계정의 이름을 리턴한다
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    // 계정의 패스워드를 리턴한다
    @Override
    public String getUsername() {
        return member.getUsername();
    }

    // 계정이 만료되지 않았는지를 리턴한다
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠겨있지 않은지를 리턴한다
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정이 만료되지 않았는지를 리턴한다
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 사용가능한 계정인지를 리턴한다
    @Override
    public boolean isEnabled() {
        return true;
    }
}
