package com.kirinit.service.flower.delivery.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDto {
    private String resultCode;
    private String resultMessage;
    private Object data;
}
