package com.kirinit.service.flower.delivery.controller;

import com.kirinit.service.flower.delivery.config.auth.PrincipalDetails;
import com.kirinit.service.flower.delivery.dto.DeliveryDto;
import com.kirinit.service.flower.delivery.entity.Delivery;
import com.kirinit.service.flower.delivery.entity.DeliverySearch;
import com.kirinit.service.flower.delivery.entity.DeliveryStatus;
import com.kirinit.service.flower.delivery.service.DeliverService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
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
        String start = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String end = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        deliverySearch.setStartDate(LocalDateTime.now());
        deliverySearch.setEndDate(LocalDateTime.now().plusDays(1));

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

        // Delivery Dto
        deliveryDto.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        model.addAttribute("deliveryDto", deliveryDto);

        return "deliveries/createDeliveryForm";
    }

    @PostMapping("/deliveries/new")
    public String create(@AuthenticationPrincipal PrincipalDetails principal,
                                 DeliveryDto deliveryDto,
                                 BindingResult result) {

        if (result.hasErrors()) {
            return "redirect:/deliveries";
        }

        // 해당 배송일자로 검색하여 최대 no값 구하기
        String start = deliveryDto.getDate();
        String end = LocalDateTime.of(LocalDate.parse(deliveryDto.getDate()).plusDays(1), LocalTime.of(0,0,0)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<Delivery> deliveries = deliverService.findDeliveries(start, end);
        Delivery delivery = deliveries.stream()
                .max(Comparator.comparing(Delivery::getNo))
                .orElse(new Delivery());
        int no = delivery.getNo() == 0 ? 1 : delivery.getNo();

        // Delivery 객체 생성
        Delivery insertDelivery = Delivery.builder()
                .member(principal.getMember())
                .no(no)
                .date(deliveryDto.getDate())
                .time(deliveryDto.getTime())
                .address(deliveryDto.getAddress())
                .toName(deliveryDto.getToName())
                .toTel(deliveryDto.getToTel())
                .itemName(deliveryDto.getItemName())
                .memo(deliveryDto.getMemo())
                .orderCompanyName(deliveryDto.getOrderCompanyName())
                .orderCompanyTel(deliveryDto.getOrderCompanyTel())
                .deliveryCompanyName(deliveryDto.getDeliveryCompanyName())
                .price(deliveryDto.getPrice())
                .dispatchNo(deliveryDto.getDispatchNo())
                .status(DeliveryStatus.READY)
                .build();
        System.out.println("insertDelivery.toString() = " + insertDelivery.toString());
        // DB에 저장
        deliverService.insert(insertDelivery);
        // 여러 배달 dto 를 받아서 저장
//        for (DeliveryDto deliveryDto : deliveryDtoList) {
//
//        }

        System.out.println("checkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
        return "redirect:/deliveries";
    }
}
