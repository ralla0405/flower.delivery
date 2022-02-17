package com.kirinit.service.flower.delivery.controller;

import com.kirinit.service.flower.delivery.dto.MemberDto;
import com.kirinit.service.flower.delivery.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/login")
    public String login() {
        return "members/login";
    }

    @GetMapping("/join")
    public String join() {
        return "members/join";
    }

    @PostMapping("/joinProc")
    public @ResponseBody String joinProc(MemberDto memberDto) {
        System.out.println(memberDto);
        memberService.join(memberDto);
        return "join";
    }

}
