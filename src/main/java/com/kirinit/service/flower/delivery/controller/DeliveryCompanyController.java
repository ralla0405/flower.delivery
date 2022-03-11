package com.kirinit.service.flower.delivery.controller;

import com.kirinit.service.flower.delivery.config.auth.PrincipalDetails;
import com.kirinit.service.flower.delivery.dto.DeliveryCompanyDto;
import com.kirinit.service.flower.delivery.dto.ResponseDto;
import com.kirinit.service.flower.delivery.entity.DeliveryCompany;
import com.kirinit.service.flower.delivery.service.DeliveryCompanyService;
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

@Controller
@RequiredArgsConstructor
public class DeliveryCompanyController {

    private final DeliveryCompanyService deliveryCompanyService;

    @GetMapping("/deliveryCompanies/new")
    public String createForm(@AuthenticationPrincipal PrincipalDetails principal,
                             Model model) {
        // 멤버 정보
        model.addAttribute("member", principal.getMember());

        // 배송업체 dto
        model.addAttribute("deliveryCompanyDto", new DeliveryCompanyDto());

        return "deliveryCompanies/createDeliveryCompanyForm";
    }

    @GetMapping("/deliveryCompanies")
    public String deliveryCompanies(@AuthenticationPrincipal PrincipalDetails principal,
                                    Model model) {
        // 멤버 정보
        model.addAttribute("member", principal.getMember());

        // 배송업체 리스트
        model.addAttribute("deliveryCompanies", deliveryCompanyService.findDeliveryCompanies());

        return "deliveryCompanies/deliveryCompanyList";
    }

    @PostMapping("/deliveryCompanies/new")
    @ResponseBody
    public ResponseEntity<ResponseDto> create(@RequestBody List<DeliveryCompanyDto> deliveryCompanyDtoList,
                                              BindingResult result) {
        if (result.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder()
                    .resultCode("9999")
                    .resultMessage("서버 오류입니다.")
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        
        // 배송업체 List 저장
        List<DeliveryCompany> insertList = new ArrayList<>();
        for (DeliveryCompanyDto deliveryCompanyDto : deliveryCompanyDtoList) {
            // name 중복 검사
            boolean isExisted = deliveryCompanyService.validateDuplicateDeliveryCompany(deliveryCompanyDto);
            if (isExisted) {
                ResponseDto responseDto = ResponseDto.builder()
                        .resultCode("8888")
                        .resultMessage("업체명은 중복 불가입니다.")
                        .build();

                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            }

            DeliveryCompany deliveryCompany = DeliveryCompany.builder()
                    .name(deliveryCompanyDto.getName())
                    .color(deliveryCompanyDto.getColor())
                    .build();
            insertList.add(deliveryCompany);
        }
        
        deliveryCompanyService.DeliveryCompany(insertList);
        
        ResponseDto responseDto = ResponseDto.builder()
                .resultCode("0000")
                .resultMessage("정상 처리 되었습니다.")
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/deliveryCompanies/edit")
    @ResponseBody
    public ResponseEntity<ResponseDto> update(@RequestBody List<DeliveryCompanyDto> deliveryCompanyDtoList,
                                                               BindingResult result) {
        if (result.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder()
                    .resultCode("9999")
                    .resultMessage("서버 오류입니다.")
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        // name 중복검사
        for (DeliveryCompanyDto deliveryCompanyDto : deliveryCompanyDtoList) {
            if (deliveryCompanyService.validateDuplicateDeliveryCompany(deliveryCompanyDto)) {
                ResponseDto responseDto = ResponseDto.builder()
                        .resultCode("8888")
                        .resultMessage(deliveryCompanyDto.getName() + " 업체명은 중복 불가입니다.")
                        .build();

                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            }
        }

        // 배송업체 List 수정
        for (DeliveryCompanyDto deliveryCompanyDto : deliveryCompanyDtoList) {
            deliveryCompanyService.updateDeliveryCompany(deliveryCompanyDto.getId(), deliveryCompanyDto.getName(), deliveryCompanyDto.getColor());
        }

        ResponseDto responseDto = ResponseDto.builder()
                .resultCode("0000")
                .resultMessage("정상 처리 되었습니다.")
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/deliveryCompanies/delete")
    @ResponseBody
    public ResponseEntity<ResponseDto> delete(@RequestBody DeliveryCompanyDto deliveryCompanyDto,
                                                               BindingResult result) {
        if (result.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder()
                    .resultCode("9999")
                    .resultMessage("서버 오류입니다.")
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        // 배송업체 삭제
        deliveryCompanyService.deleteDeliveryCompany(deliveryCompanyDto.getId());

        ResponseDto responseDto = ResponseDto.builder()
                .resultCode("0000")
                .resultMessage("정상 처리 되었습니다.")
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
