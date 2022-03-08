package com.kirinit.service.flower.delivery.controller;

import com.kirinit.service.flower.delivery.config.auth.PrincipalDetails;
import com.kirinit.service.flower.delivery.dto.DeliveryDto;
import com.kirinit.service.flower.delivery.dto.FeeDto;
import com.kirinit.service.flower.delivery.dto.ResponseDto;
import com.kirinit.service.flower.delivery.dto.TelDto;
import com.kirinit.service.flower.delivery.entity.*;
import com.kirinit.service.flower.delivery.service.DeliveryService;
import com.kirinit.service.flower.delivery.service.DeliveryCompanyService;
import com.kirinit.service.flower.delivery.service.DeliveryFeeService;
import com.kirinit.service.flower.delivery.service.OrderCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final DeliveryCompanyService deliveryCompanyService;
    private final DeliveryFeeService deliveryFeeService;
    private final OrderCompanyService orderCompanyService;

    @GetMapping("/deliveries")
    public String deliveries(@AuthenticationPrincipal PrincipalDetails principal,
                             @ModelAttribute("deliverySearch") DeliverySearch deliverySearch,
                             Model model) {
        // 멤버 정보
        model.addAttribute("member", principal.getMember());

        // 배송업체 정보
        System.out.println("deliveryCompanyService.findDeliveryCompanies() = " + deliveryCompanyService.findDeliveryCompanies());
        model.addAttribute("deliveryCompanies", deliveryCompanyService.findDeliveryCompanies());

        // 배송조회 기간
        String start = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String end = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        deliverySearch.setStartDate(LocalDateTime.now());
        deliverySearch.setEndDate(LocalDateTime.now().plusDays(1));

        // 배송리스트
        List<Delivery> deliveries = deliveryService.findDeliveries(start, end);
        model.addAttribute("deliveries", deliveries);

        return "deliveries/deliveryList";
    }

    @GetMapping("/deliveries/new")
    public String createForm(@AuthenticationPrincipal PrincipalDetails principal,
                             @ModelAttribute("deliveryDto") DeliveryDto deliveryDto,
                             Model model) {

        // 멤버 정보
        model.addAttribute("member", principal.getMember());

        // 배송업체 정보
        model.addAttribute("deliveryCompanies", deliveryCompanyService.findDeliveryCompanies());

        // 배달 Dto
        deliveryDto.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        model.addAttribute("deliveryDto", deliveryDto);

        return "deliveries/createDeliveryForm";
    }

    @PostMapping("/deliveries/new")
    @ResponseBody
    public ResponseEntity<ResponseDto> create(@AuthenticationPrincipal PrincipalDetails principal,
                                              @RequestBody List<DeliveryDto> deliveryDtoList,
                                              BindingResult result) {

        if (result.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder()
                    .resultCode("9999")
                    .resultMessage("서버 오류입니다.")
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        // 여러 배달 dto 를 받아서 저장
        for (DeliveryDto deliveryDto : deliveryDtoList) {

            // 해당 배송일자로 검색하여 최대 no값 구하기
            String start = deliveryDto.getDate();
            String end = LocalDateTime.of(
                    LocalDate.parse(deliveryDto.getDate()).plusDays(1),
                    LocalTime.of(0,0,0)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            List<Delivery> deliveries = deliveryService.findDeliveries(start, end);
            Delivery delivery = deliveries.stream()
                    .max(Comparator.comparing(Delivery::getNo))
                    .orElse(new Delivery());
            int no = delivery.getNo() == 0 ? 1 : (delivery.getNo() + 1);

            // DeliveryCompany 조회
            Optional<DeliveryCompany> findDeliveryCompany = deliveryCompanyService.findOne(deliveryDto.getDeliveryCompanyDto().getId());

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
                    .deliveryCompany(findDeliveryCompany.get())
                    .price(deliveryDto.getPrice())
                    .dispatchNo(deliveryDto.getDispatchNo())
                    .status(DeliveryStatus.READY)
                    .build();
            // DB에 저장
            deliveryService.Delivery(insertDelivery);
        }

        ResponseDto responseDto = ResponseDto.builder()
                .resultCode("0000")
                .resultMessage("정상 처리 되었습니다.")
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/deliveries/edit")
    @ResponseBody
    public ResponseEntity<ResponseDto> create(@RequestBody List<DeliveryDto> deliveryDtoList,
                                              BindingResult result) {

        if (result.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder()
                    .resultCode("9999")
                    .resultMessage("서버 오류입니다.")
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        // 여러 배달 dto 를 받아서 수정
        for (DeliveryDto deliveryDto : deliveryDtoList) {
            // 수정전 값 조회
            Optional<Delivery> findDelivery = deliveryService.findOne(deliveryDto.getId());
            // 기존 date 가 다르면 no 변경
            int no = findDelivery.get().getNo();
            if (!findDelivery.get().getDate().equals(deliveryDto.getDate())) {
                String start = deliveryDto.getDate();
                String end = LocalDateTime.of(
                        LocalDate.parse(deliveryDto.getDate()).plusDays(1),
                        LocalTime.of(0,0,0)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                List<Delivery> deliveries = deliveryService.findDeliveries(start, end);
                Delivery delivery = deliveries.stream()
                        .max(Comparator.comparing(Delivery::getNo))
                        .orElse(new Delivery());
                no = delivery.getNo() == 0 ? 1 : (delivery.getNo() + 1);
            }

            deliveryService.updateDelivery(deliveryDto.getId(), no, deliveryDto.getDate(), deliveryDto.getTime(), deliveryDto.getAddress(), deliveryDto.getToTel(), deliveryDto.getToName(), deliveryDto.getItemName(), deliveryDto.getMemo(), deliveryDto.getOrderCompanyName(), deliveryDto.getOrderCompanyTel(), deliveryDto.getDeliveryCompanyDto().getId(), deliveryDto.getPrice(), deliveryDto.getDispatchNo());
        }

        ResponseDto responseDto = ResponseDto.builder()
                .resultCode("0000")
                .resultMessage("정상 처리 되었습니다.")
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/deliveries/delete")
    @ResponseBody
    public ResponseEntity<ResponseDto> create(@RequestBody DeliveryDto deliveryDto,
                                              BindingResult result) {

        if (result.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder()
                    .resultCode("9999")
                    .resultMessage("서버 오류입니다.")
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        // 배송 삭제
        deliveryService.deleteDelivery(deliveryDto.getId());

        ResponseDto responseDto = ResponseDto.builder()
                .resultCode("0000")
                .resultMessage("정상 처리 되었습니다.")
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/deliveries/getFee")
    @ResponseBody
    public ResponseEntity<ResponseDto> create(@RequestBody FeeDto feeDto,
                                              BindingResult result) {

        if (result.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder()
                    .resultCode("9999")
                    .resultMessage("서버 오류입니다.")
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        // 배송비 계산하여 반환
        int price = 0;
        System.out.println("feeDto = " + feeDto.getId());
        Optional<DeliveryCompany> deliveryCompany = deliveryCompanyService.findOne(Long.valueOf(feeDto.getId()));
        List<DeliveryFee> deliveryFees = deliveryFeeService.findDeliveryFeesByDeliveryCompany(deliveryCompany.get());
        for (DeliveryFee deliveryFee : deliveryFees) {
            if (deliveryFee.getAreaName().contains(feeDto.getAddress())) {
                price = deliveryFee.getPrice();
            }
        }

        HashMap<String, Integer> data = new HashMap<>();
        data.put("price", price);
        ResponseDto responseDto = ResponseDto.builder()
                .resultCode("0000")
                .resultMessage("정상 처리 되었습니다.")
                .data(data)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/deliveries/getTel")
    @ResponseBody
    public ResponseEntity<ResponseDto> create(@RequestBody TelDto telDto,
                                              BindingResult result) {

        if (result.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder()
                    .resultCode("9999")
                    .resultMessage("서버 오류입니다.")
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        // 발주업체 연락처 반환
        String tel = "";
        Optional<OrderCompany> findOrderCompany = orderCompanyService.findByName(telDto.getName());
        tel = findOrderCompany.orElse(new OrderCompany()).getTel();

        HashMap<String, String> data = new HashMap<>();
        data.put("tel", tel);
        ResponseDto responseDto = ResponseDto.builder()
                .resultCode("0000")
                .resultMessage("정상 처리 되었습니다.")
                .data(data)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
