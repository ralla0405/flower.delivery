package com.kirinit.service.flower.delivery.controller;

import com.kirinit.service.flower.delivery.config.auth.PrincipalDetails;
import com.kirinit.service.flower.delivery.dto.OrderCompanyDto;
import com.kirinit.service.flower.delivery.dto.ResponseDto;
import com.kirinit.service.flower.delivery.entity.OrderCompany;
import com.kirinit.service.flower.delivery.service.OrderCompanyService;
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
public class OrderCompanyController {

    private final OrderCompanyService orderCompanyService;

    @GetMapping("/orderCompanies/new")
    public String createForm(@AuthenticationPrincipal PrincipalDetails principal,
                             Model model) {
        // 멤버 정보
        model.addAttribute("member", principal.getMember());

        // 발주업체 dto
        model.addAttribute("orderCompanyDto", new OrderCompanyDto());

        return "orderCompanies/createOrderCompanyForm";
    }

    @GetMapping("/orderCompanies")
    public String orderCompanies(@AuthenticationPrincipal PrincipalDetails principal,
                                 Model model) {
        // 멤버 정보
        model.addAttribute("member", principal.getMember());

        // 발주업체 리스트
        model.addAttribute("orderCompanies", orderCompanyService.findOrderCompanies());

        return "orderCompanies/orderCompanyList";
    }

    @PostMapping("/orderCompanies/new")
    @ResponseBody
    public ResponseEntity<ResponseDto> create(@RequestBody List<OrderCompanyDto> orderCompanyDtoList,
                                              BindingResult result) {
        if (result.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder()
                    .resultCode("9999")
                    .resultMessage("서버 오류입니다.")
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        // 발주업체 List 저장
        List<OrderCompany> insertList = new ArrayList<>();
        for (OrderCompanyDto orderCompanyDto : orderCompanyDtoList) {
            // name 중복 검사
            boolean isExisted = orderCompanyService.validateDuplicateOrderCompany(orderCompanyDto);
            if (isExisted) {
                ResponseDto responseDto = ResponseDto.builder()
                        .resultCode("8888")
                        .resultMessage("업체명은 중복 불가입니다.")
                        .build();

                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            }

            OrderCompany orderCompany = OrderCompany.builder()
                    .name(orderCompanyDto.getName())
                    .tel(orderCompanyDto.getTel())
                    .build();
            insertList.add(orderCompany);
        }

        orderCompanyService.OrderCompany(insertList);

        ResponseDto responseDto = ResponseDto.builder()
                .resultCode("0000")
                .resultMessage("정상 처리 되었습니다.")
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/orderCompanies/edit")
    @ResponseBody
    public ResponseEntity<ResponseDto> update(@RequestBody List<OrderCompanyDto> orderCompanyDtoList,
                                              BindingResult result) {
        if (result.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder()
                    .resultCode("9999")
                    .resultMessage("서버 오류입니다.")
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        // name 중복검사
        for (OrderCompanyDto orderCompanyDto : orderCompanyDtoList) {
            if (orderCompanyService.validateDuplicateOrderCompany(orderCompanyDto)) {
                ResponseDto responseDto = ResponseDto.builder()
                        .resultCode("8888")
                        .resultMessage(orderCompanyDto.getName() + " 업체명은 중복 불가입니다.")
                        .build();

                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            }
        }

        // 발주업체 List 수정
        for (OrderCompanyDto orderCompanyDto : orderCompanyDtoList) {
            orderCompanyService.updateOrderCompany(orderCompanyDto.getId(), orderCompanyDto.getName(), orderCompanyDto.getTel());
        }

        ResponseDto responseDto = ResponseDto.builder()
                .resultCode("0000")
                .resultMessage("정상 처리 되었습니다.")
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/orderCompanies/delete")
    @ResponseBody
    public ResponseEntity<ResponseDto> delete(@RequestBody OrderCompanyDto orderCompanyDto,
                                              BindingResult result) {
        if (result.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder()
                    .resultCode("9999")
                    .resultMessage("서버 오류입니다.")
                    .build();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        // 발주업체 삭제
        orderCompanyService.deleteOrderCompany(orderCompanyDto.getId());

        ResponseDto responseDto = ResponseDto.builder()
                .resultCode("0000")
                .resultMessage("정상 처리 되었습니다.")
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
