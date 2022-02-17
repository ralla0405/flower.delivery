package com.kirinit.service.flower.delivery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DeliveryController {

    @GetMapping("/deliveries")
    public String deliveries() {
        return "/deliveries/deliveryList";
    }
}
