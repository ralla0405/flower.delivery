package com.kirinit.service.flower.delivery.controller;

import com.kirinit.service.flower.delivery.config.auth.PrincipalDetails;
import com.kirinit.service.flower.delivery.dto.MemberDto;
import com.kirinit.service.flower.delivery.entity.Member;
import com.kirinit.service.flower.delivery.entity.MemberRole;
import com.kirinit.service.flower.delivery.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/login")
    public String login(@AuthenticationPrincipal PrincipalDetails principal) {
        if (principal != null) {
            return "redirect:/deliveries";
        } else {
            return "members/login";
        }
    }

    @GetMapping("/members/new")
    public String createForm(@AuthenticationPrincipal PrincipalDetails principal, Model model) {
        for (GrantedAuthority auth : principal.getAuthorities()) {
            if (Objects.equals(auth.getAuthority(), MemberRole.ROLE_USER.name())) {
                System.out.println("auth.getAuthority() = " + auth.getAuthority());
            }
        }
        model.addAttribute("memberDto", new MemberDto());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    @Secured("ROLE_ADMIN")
    public String joinProc(MemberDto memberDto, BindingResult result) {

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Member member = Member.builder()
            .username(memberDto.getUsername())
            .password(memberDto.getPassword())
            .name(memberDto.getName())
            .build();

        memberService.join(member);
        return "redirect:/login";
    }

    @GetMapping("/members")
    public String list(Model model) {
        model.addAttribute("members", memberService.findMembers());
        return "members/memberList";
    }

}
