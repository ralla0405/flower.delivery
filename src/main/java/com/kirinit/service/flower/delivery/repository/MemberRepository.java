package com.kirinit.service.flower.delivery.repository;

import com.kirinit.service.flower.delivery.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByUsername(String username);
}
