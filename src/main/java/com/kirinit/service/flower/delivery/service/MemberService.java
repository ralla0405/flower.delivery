package com.kirinit.service.flower.delivery.service;

import com.kirinit.service.flower.delivery.dto.MemberDto;
import com.kirinit.service.flower.delivery.entity.Member;
import com.kirinit.service.flower.delivery.entity.MemberRole;
import com.kirinit.service.flower.delivery.repository.MemberRepository;
import com.kirinit.service.flower.delivery.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * join
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        Optional<Member> findMember = memberRepository.findMemberByUsername(member.getUsername());
        if (findMember.isPresent()) {
            // 존재하는 회원인걸 표시해야함
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

}
