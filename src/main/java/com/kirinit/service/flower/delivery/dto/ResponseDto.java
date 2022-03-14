package com.kirinit.service.flower.delivery.dto;

import com.kirinit.service.flower.delivery.entity.Delivery;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
@Builder
public class ResponseDto {
    private String resultCode;
    private String resultMessage;
    private Object data;
}
