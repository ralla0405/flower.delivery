package com.kirinit.service.flower.delivery.controller;

import com.kirinit.service.flower.delivery.config.auth.PrincipalDetails;
import com.kirinit.service.flower.delivery.dto.DeliveryCompanyDto;
import com.kirinit.service.flower.delivery.dto.DeliveryFeeDto;
import com.kirinit.service.flower.delivery.dto.ResponseDto;
import com.kirinit.service.flower.delivery.entity.DeliveryCompany;
import com.kirinit.service.flower.delivery.entity.DeliveryFee;
import com.kirinit.service.flower.delivery.service.DeliveryCompanyService;
import com.kirinit.service.flower.delivery.service.DeliveryFeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class DeliveryFeeController {

    private final DeliveryFeeService deliveryFeeService;
    private final DeliveryCompanyService deliveryCompanyService;

    @GetMapping("/deliveryFees/new")
    public String createForm(@AuthenticationPrincipal PrincipalDetails principal,
                             Model model) {

        // 멤버 정보
        model.addAttribute("member", principal.getMember());

        // 배송업체 정보
        model.addAttribute("deliveryCompanies", deliveryCompanyService.findDeliveryCompanies());

        // 배송비 dto
        model.addAttribute("deliveryFeeDto", new DeliveryFeeDto());

        return "deliveryFees/createDeliveryFeeForm";
    }

    @GetMapping("/deliveryFees")
    public String deliveryFees(@AuthenticationPrincipal PrincipalDetails principal,
                                    Model model) {

        // 멤버 정보
        model.addAttribute("member", principal.getMember());

        // 배송업체 리스트
        model.addAttribute("deliveryCompanies", deliveryCompanyService.findDeliveryCompanies());

        // 배송비 리스트
        model.addAttribute("deliveryFees", deliveryFeeService.findDeliveryFees());

        return "deliveryFees/deliveryFeeList";
    }

    @PostMapping("/deliveryFees/new")
    @ResponseBody
    public ResponseEntity<ResponseDto> create(@RequestBody List<DeliveryFeeDto> deliveryFeeList,
                                              BindingResult result) {

        if (result.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder()
                    .resultCode("9999")
                    .resultMessage("서버 오류입니다.")
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        // 배송비 List 저장
        List<DeliveryFee> insertList = new ArrayList<>();
        for (DeliveryFeeDto deliveryFeeDto : deliveryFeeList) {
            // areaName 중복검사
            boolean isExisted = deliveryFeeService.validateDuplicateAreaName(deliveryFeeDto);
            if (isExisted) {
                ResponseDto responseDto = ResponseDto.builder()
                        .resultCode("8888")
                        .resultMessage(deliveryFeeDto.getDeliveryCompanyDto().getName() + " 업체의 " + deliveryFeeDto.getAreaName() + " 구역명은 중복 불가입니다.")
                        .build();

                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            }

            Optional<DeliveryCompany> findDeliveryCompany = deliveryCompanyService.findOne(deliveryFeeDto.getDeliveryCompanyDto().getId());
            DeliveryFee deliveryFee = DeliveryFee.builder()
                    .deliveryCompany(findDeliveryCompany.get())
                    .areaName(deliveryFeeDto.getAreaName())
                    .price(deliveryFeeDto.getPrice())
                    .build();
            insertList.add(deliveryFee);
        }

        deliveryFeeService.DeliverFee(insertList);

        ResponseDto responseDto = ResponseDto.builder()
                .resultCode("0000")
                .resultMessage("정상 처리 되었습니다.")
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/deliveryFees/edit")
    @ResponseBody
    public ResponseEntity<ResponseDto> update(@RequestBody List<DeliveryFeeDto> deliveryFeeList,
                                              BindingResult result) {

        if (result.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder()
                    .resultCode("9999")
                    .resultMessage("서버 오류입니다.")
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        // areaName 중복검사
        for (DeliveryFeeDto deliveryFeeDto : deliveryFeeList) {
            boolean isExisted = deliveryFeeService.validateDuplicateAreaName(deliveryFeeDto);
            if (isExisted) {
                ResponseDto responseDto = ResponseDto.builder()
                        .resultCode("8888")
                        .resultMessage(deliveryFeeDto.getDeliveryCompanyDto().getName() + " 업체의 " + deliveryFeeDto.getAreaName() + " 구역명은 중복 불가입니다.")
                        .build();

                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            }
        }

        // 배송비 List 수정
        for (DeliveryFeeDto deliveryFeeDto : deliveryFeeList) {
            deliveryFeeService.updateDeliveryFee(deliveryFeeDto.getId(),
                    deliveryFeeDto.getDeliveryCompanyDto().getId(),
                    deliveryFeeDto.getAreaName(),
                    deliveryFeeDto.getPrice());
        }

        ResponseDto responseDto = ResponseDto.builder()
                .resultCode("0000")
                .resultMessage("정상 처리 되었습니다.")
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/deliveryFees/delete")
    @ResponseBody
    public ResponseEntity<ResponseDto> delete(@RequestBody DeliveryFeeDto deliveryFeeDto,
                                              BindingResult result) {
        if (result.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder()
                    .resultCode("9999")
                    .resultMessage("서버 오류입니다.")
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        // 배송업체 삭제
        deliveryFeeService.deleteDeliveryFee(deliveryFeeDto.getId());

        ResponseDto responseDto = ResponseDto.builder()
                .resultCode("0000")
                .resultMessage("정상 처리 되었습니다.")
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}