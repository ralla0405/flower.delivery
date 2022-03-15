package com.kirinit.service.flower.delivery.service;

import com.kirinit.service.flower.delivery.dto.MemberDto;
import com.kirinit.service.flower.delivery.entity.Member;
import com.kirinit.service.flower.delivery.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

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
     * 회원등록
     */
    @Transactional
    public void member(List<Member> members) {
        System.out.println("members = " + members);
        memberRepository.saveAll(members);
    }

    /**
     * 회원아이디 중복 검토
     */
    public boolean validateDuplicateMember(MemberDto memberDto) {
        Optional<Member> findMember = memberRepository.findMemberByUsername(memberDto.getUsername());
        if (findMember.isPresent()) {
            if (memberDto.getId() != null) {
                return !findMember.get().getUsername().equals(memberDto.getUsername());
            }
            return true;
        }
        return false;
    }

    /**
     * 회원 수정
     */
    @Transactional
    public void updateMember(Long memberId, String username, String name, String password) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        System.out.println("findMember = " + findMember);
        System.out.println("username = " + username);
        System.out.println("name = " + name);
        System.out.println("password = " + password);
        findMember.get().change(username, name, password);
    }

    /**
     * 회원 삭제
     */
    @Transactional
    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }
}
