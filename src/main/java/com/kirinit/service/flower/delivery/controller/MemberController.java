package com.kirinit.service.flower.delivery.controller;

import com.kirinit.service.flower.delivery.config.auth.PrincipalDetails;
import com.kirinit.service.flower.delivery.dto.MemberDto;
import com.kirinit.service.flower.delivery.dto.ResponseDto;
import com.kirinit.service.flower.delivery.entity.Member;
import com.kirinit.service.flower.delivery.entity.MemberRole;
import com.kirinit.service.flower.delivery.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping(value = {"/login", "/"})
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception,
                        @AuthenticationPrincipal PrincipalDetails principal,
                        Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        if (principal != null) {
            return "redirect:/deliveries";
        } else {
            return "members/login";
        }
    }

    @GetMapping("/members/new")
    public String createForm(@AuthenticationPrincipal PrincipalDetails principal, Model model) {

        // 멤버 정보
        model.addAttribute("member", principal.getMember());

        // Member dto
        model.addAttribute("memberDto", new MemberDto());

        return "members/createMemberForm";
    }

    @GetMapping("/members")
    public String list(@AuthenticationPrincipal PrincipalDetails principal,
                       Model model) {

        // 멤버 정보
        model.addAttribute("member", principal.getMember());

        // 멤버 리스트
        model.addAttribute("members", memberService.findMembers());

        return "members/memberList";
    }

    @PostMapping("/members/new")
    @ResponseBody
    public ResponseEntity<ResponseDto> create(@RequestBody List<MemberDto> memberDtoList,
                                              BindingResult result) {

        if (result.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder()
                    .resultCode("9999")
                    .resultMessage("서버 오류입니다.")
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        // 멤버 List 저장
        List<Member> insertList = new ArrayList<>();
        for (MemberDto memberDto : memberDtoList) {
            boolean isExisted = memberService.validateDuplicateMember(memberDto);
            if (isExisted) {
                ResponseDto responseDto = ResponseDto.builder()
                        .resultCode("8888")
                        .resultMessage(memberDto.getUsername() + " 아이디는 중복 불가입니다.")
                        .build();

                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            }

            Member member = Member.builder()
                    .name(memberDto.getName())
                    .username(memberDto.getUsername())
                    .password(passwordEncoder.encode(memberDto.getPassword()))
                    .role(MemberRole.ROLE_USER)
                    .build();
            insertList.add(member);
        }

        memberService.member(insertList);

        ResponseDto responseDto = ResponseDto.builder()
                .resultCode("0000")
                .resultMessage("정상 처리 되었습니다.")
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/members/edit")
    @ResponseBody
    public ResponseEntity<ResponseDto> update(@RequestBody List<MemberDto> memberDtoList,
                                              BindingResult result) {

        if (result.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder()
                    .resultCode("9999")
                    .resultMessage("서버 오류입니다.")
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        // 멤버 아이디 중복검사
        for (MemberDto memberDto : memberDtoList) {
            boolean isExisted = memberService.validateDuplicateMember(memberDto);
            if (isExisted) {
                ResponseDto responseDto = ResponseDto.builder()
                        .resultCode("8888")
                        .resultMessage(memberDto.getUsername() + " 아이디는 중복 불가입니다.")
                        .build();

                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            }
        }

        // 멤버 List 수정
        for (MemberDto memberDto : memberDtoList) {
            memberService.updateMember(
                memberDto.getId(),
                memberDto.getUsername(),
                memberDto.getName(),
                passwordEncoder.encode(memberDto.getPassword())
            );
        }

        ResponseDto responseDto = ResponseDto.builder()
                .resultCode("0000")
                .resultMessage("정상 처리 되었습니다.")
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/members/delete")
    @ResponseBody
    public ResponseEntity<ResponseDto> delete(@RequestBody MemberDto memberDto,
                                              BindingResult result) {

        if (result.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder()
                    .resultCode("9999")
                    .resultMessage("서버 오류입니다.")
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        memberService.deleteMember(memberDto.getId());

        ResponseDto responseDto = ResponseDto.builder()
                .resultCode("0000")
                .resultMessage("정상 처리 되었습니다.")
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
