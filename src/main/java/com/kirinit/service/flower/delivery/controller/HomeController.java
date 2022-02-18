package com.kirinit.service.flower.delivery.controller;

import com.kirinit.service.flower.delivery.config.auth.PrincipalDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal PrincipalDetails principal) {
        if (principal != null) {
            for (GrantedAuthority auth : principal.getAuthorities()) {
                // 권한이 ADMIN 일때 loginHeader -> adminHeader
                System.out.println("auth.getAuthority() = " + auth.getAuthority());
            }
            return "redirect:/deliveries";
        } else {
            return "home";
        }
    }

}
