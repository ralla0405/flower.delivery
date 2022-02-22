package com.kirinit.service.flower.delivery.controller;

import com.kirinit.service.flower.delivery.config.auth.PrincipalDetails;
import com.kirinit.service.flower.delivery.dto.DeliveryDto;
import com.kirinit.service.flower.delivery.entity.Delivery;
import com.kirinit.service.flower.delivery.entity.DeliverySearch;
import com.kirinit.service.flower.delivery.service.DeliverService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliverService deliverService;

    @GetMapping("/deliveries")
    public String deliveries(@AuthenticationPrincipal PrincipalDetails principal,
                             @ModelAttribute("deliverySearch") DeliverySearch deliverySearch,
                             Model model) {
        // 멤버 정보
        model.addAttribute("member", principal.getMember());

        // 배송조회 기간
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(0,0,0));
        deliverySearch.setStartDate(start);
        deliverySearch.setEndDate(end);

        // 배송리스트
        List<Delivery> deliveries = deliverService.findDeliveries(start, end);
        model.addAttribute("deliveries", deliveries);

        return "deliveries/deliveryList";
    }

    @GetMapping("/deliveries/new")
    public String createForm(@AuthenticationPrincipal PrincipalDetails principal,
                             @ModelAttribute("deliveryDto") DeliveryDto deliveryDto,
                             Model model) {

        // 멤버 정보
        model.addAttribute("member", principal.getMember());

        // 배달 dto
//        DeliveryDto deliveryDto = new DeliveryDto();
//        deliveryDto.setDate();
        deliveryDto.setDate(LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0)));
        model.addAttribute("deliveryDto", deliveryDto);

        return "deliveries/createDeliveryForm";
    }

    @PostMapping("/delvieries/new")
    public String create(@Valid DeliveryDto deliveryDto, BindingResult result) {

        if (result.hasErrors()) {
            return "delvieries/createDeliveryForm";
        }

        // 여러 배달 dto 를 받아서 저장

        // DB에 저장하기 전 해당 날짜(배송일자 date) 중 제일 큰 no 값 불러와 + 1 한 후 생성
//        Delivery delivery = Delivery.builder()
//                .no()
        return "redirect:/deliveries";
    }
}
