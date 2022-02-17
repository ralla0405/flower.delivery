package com.kirinit.service.flower.delivery.service;

import com.kirinit.service.flower.delivery.dto.MemberDto;
import com.kirinit.service.flower.delivery.entity.Member;
import com.kirinit.service.flower.delivery.entity.MemberRole;
import com.kirinit.service.flower.delivery.repository.MemberRepository;
import com.kirinit.service.flower.delivery.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * join
     */
    @Transactional
    public Long join(MemberDto memberDto) {
        validateDuplicateMember(memberDto);
        Member member = Member.builder()
            .username(memberDto.getUsername())
            .name(memberDto.getName())
            .password(passwordEncoder.encode(memberDto.getPassword()))
            .role(MemberRole.ROLE_USER)
            .build();

        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(MemberDto memberDto) {
        if (memberRepository.findMemberByUsername(memberDto.getUsername()).isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * Member 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * Member 단일 조회
     */
    public Optional<Member> findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    /**
     * Authority 포함 단일 조회
     */
    @Transactional(readOnly = true)
    public Optional<Member> getMemberWithAuthorities(String userId) {
        return memberRepository.findOneWithAuthoritiesByUsername(userId);

    }

    @Transactional(readOnly = true)
    public Optional<Member> getMyMemberWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(memberRepository::findOneWithAuthoritiesByUsername);
    }
}
