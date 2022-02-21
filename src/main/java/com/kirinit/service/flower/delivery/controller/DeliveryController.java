package com.kirinit.service.flower.delivery.controller;

import com.kirinit.service.flower.delivery.config.auth.PrincipalDetails;
import com.kirinit.service.flower.delivery.entity.Delivery;
import com.kirinit.service.flower.delivery.repository.DeliverySearch;
import com.kirinit.service.flower.delivery.service.DeliverService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

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
        model.addAttribute("member", principal.getMember());

        LocalDateTime start = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59));
        deliverySearch.setStartDate(start);
        deliverySearch.setEndDate(end);
        List<Delivery> deliveries = deliverService.findDeliveries(start, end);
        model.addAttribute("deliveries", deliveries);
        return "deliveries/deliveryList";
    }
}
