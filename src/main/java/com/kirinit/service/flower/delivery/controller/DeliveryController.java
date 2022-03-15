package com.kirinit.service.flower.delivery.controller;

import com.kirinit.service.flower.delivery.config.auth.PrincipalDetails;
import com.kirinit.service.flower.delivery.dto.*;
import com.kirinit.service.flower.delivery.entity.*;
import com.kirinit.service.flower.delivery.service.DeliveryService;
import com.kirinit.service.flower.delivery.service.DeliveryCompanyService;
import com.kirinit.service.flower.delivery.service.DeliveryFeeService;
import com.kirinit.service.flower.delivery.service.OrderCompanyService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
        model.addAttribute("deliveryCompanies", deliveryCompanyService.findDeliveryCompanies());

        // 배송리스트
        List<Delivery> deliveries = deliveryService.findDeliveries(deliverySearch);
        model.addAttribute("deliveries", deliveries);

        return "deliveries/deliveryList";
    }

    @GetMapping("/deliveries/{deliveryId}/receipt")
    public String receipt(@PathVariable("deliveryId") Long deliveryId,
                          Model model) {
        Optional<Delivery> findOne = deliveryService.findOne(deliveryId);

        ReceiptDto receiptDto = ReceiptDto.builder()
                .no(findOne.get().getNo())
                .date(findOne.get().getDate())
                .time(findOne.get().getTime())
                .itemName(findOne.get().getItemName())
                .toTel(findOne.get().getToTel())
                .memo(findOne.get().getMemo())
                .address(findOne.get().getAddress())
                .orderCompanyTel(findOne.get().getOrderCompanyTel())
                .build();

        model.addAttribute("receipt", receiptDto);

        return "deliveries/receipt";
    }

    @GetMapping("/deliveries/map")
    public String map() {
        return "deliveries/map";
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
            String end = deliveryDto.getDate();

            DeliverySearch deliverySearch = DeliverySearch.builder()
                    .startDate(start)
                    .endDate(end)
                    .build();

            List<Delivery> deliveries = deliveryService.findDeliveries(deliverySearch);
            Delivery delivery = deliveries.stream()
                    .max(Comparator.comparing(Delivery::getNo))
                    .orElse(new Delivery());
            int no = delivery.getNo() == 0 ? 1 : (delivery.getNo() + 1);

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
                    .color("#fff")
                    .price(deliveryDto.getPrice())
                    .dispatchNo(deliveryDto.getDispatchNo())
                    .status(DeliveryStatus.READY)
                    .build();
            // DB에 저장
            deliveryService.delivery(insertDelivery);
        }

        ResponseDto responseDto = ResponseDto.builder()
                .resultCode("0000")
                .resultMessage("정상 처리 되었습니다.")
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/deliveries/edit")
    @ResponseBody
    public ResponseEntity<ResponseDto> edit(@RequestBody List<DeliveryDto> deliveryDtoList,
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
                String end = deliveryDto.getDate();

                // de
                DeliverySearch deliverySearch = DeliverySearch.builder()
                        .startDate(start)
                        .endDate(end)
                        .build();

                List<Delivery> deliveries = deliveryService.findDeliveries(deliverySearch);
                Delivery delivery = deliveries.stream()
                        .max(Comparator.comparing(Delivery::getNo))
                        .orElse(new Delivery());
                no = delivery.getNo() == 0 ? 1 : (delivery.getNo() + 1);
            }

            deliveryService.updateDelivery(deliveryDto.getId(), no, deliveryDto.getDate(), deliveryDto.getTime(), deliveryDto.getAddress(), deliveryDto.getToTel(), deliveryDto.getToName(), deliveryDto.getItemName(), deliveryDto.getMemo(), deliveryDto.getOrderCompanyName(), deliveryDto.getOrderCompanyTel(), deliveryDto.getDeliveryCompanyName(), deliveryDto.getPrice(), deliveryDto.getDispatchNo());
        }

        ResponseDto responseDto = ResponseDto.builder()
                .resultCode("0000")
                .resultMessage("정상 처리 되었습니다.")
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/deliveries/delete")
    @ResponseBody
    public ResponseEntity<ResponseDto> delete(@RequestBody DeliveryDto deliveryDto,
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
    public ResponseEntity<ResponseDto> getFee(@RequestBody FeeDto feeDto,
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
        if (feeDto.getId() != "") {
            Optional<DeliveryCompany> deliveryCompany = deliveryCompanyService.findOne(Long.valueOf(feeDto.getId()));
            if (deliveryCompany.isPresent()) {
                List<DeliveryFee> deliveryFees = deliveryFeeService.findDeliveryFeesByDeliveryCompany(deliveryCompany.get());
                for (DeliveryFee deliveryFee : deliveryFees) {
                    if (deliveryFee.getAreaName().contains(feeDto.getAddress())) {
                        price = deliveryFee.getPrice();
                    }
                }
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
    public ResponseEntity<ResponseDto> getTel(@RequestBody TelDto telDto,
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

    @PostMapping("/deliveries/status")
    @ResponseBody
    public ResponseEntity<ResponseDto> status(@RequestBody StatusDto statusDto,
                                              BindingResult result) {

        if (result.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder()
                    .resultCode("9999")
                    .resultMessage("서버 오류입니다.")
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        // 배송 상태 변경
        deliveryService.updateStatus(Long.valueOf(statusDto.getId()), DeliveryStatus.valueOf(statusDto.getStatus()));

        // 배송업체 조회 color
        Optional<DeliveryCompany> findDeliveryCompany = deliveryCompanyService.findColor(statusDto.getDeliveryCompanyName());

        HashMap<String, String> data = new HashMap<>();

        // 배송 상태에 따라 배달 색상 변경
        if (statusDto.getStatus().equals("READY")) {
            deliveryService.updateColor(Long.valueOf(statusDto.getId()), "#fff");
        } else if (statusDto.getStatus().equals("COM")) {
            if (findDeliveryCompany.isPresent()) {
                data.put("color", findDeliveryCompany.get().getColor());
                deliveryService.updateColor(Long.valueOf(statusDto.getId()), findDeliveryCompany.get().getColor());
            } else {
                data.put("color", "#FFCCCC");
                deliveryService.updateColor(Long.valueOf(statusDto.getId()), "#FFCCCC");
            }
        } else if (statusDto.getStatus().equals("CHECK")) {
            deliveryService.updateColor(Long.valueOf(statusDto.getId()), "#808080");
        }

        ResponseDto responseDto = ResponseDto.builder()
                .resultCode("0000")
                .resultMessage("정상 처리 되었습니다.")
                .data(data)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/deliveries/map")
    @ResponseBody
    public ResponseEntity<ResponseDto> maps(@RequestBody DeliverySearchDto deliverySearchDto,
                                            BindingResult result) {

        if (result.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder()
                    .resultCode("9999")
                    .resultMessage("서버 오류입니다.")
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        // 배송 전체 조회
        DeliverySearch deliverySearch = DeliverySearch.builder()
                .deliveryStatus(DeliveryStatus.valueOf(deliverySearchDto.getDeliveryStatus()))
                .startDate(deliverySearchDto.getStartDate())
                .endDate(deliverySearchDto.getEndDate())
                .build();
        List<Delivery> deliveries = deliveryService.findDeliveries(deliverySearch);

        List<MapDto> collect = deliveries.stream()
                .map(d -> new MapDto(d.getNo(), d.getTime(), d.getItemName(), d.getAddress()))
                .collect(Collectors.toList());

        ResponseDto responseDto = ResponseDto.builder()
                .resultCode("0000")
                .resultMessage("정상 처리 되었습니다.")
                .data(collect)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Data
    @AllArgsConstructor
    static class MapDto {
        private int no;
        private String time;
        private String itemName;
        private String address;
    }
}
