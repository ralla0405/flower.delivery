package com.kirinit.service.flower.delivery.entity;

public enum DeliveryStatus {
    // READY - 미배차, COM - 배차, CHECK - 인수체크
    READY("미배차"),
    COM("배차"),
    CHECK("인수체크");

    private String krName;

    DeliveryStatus(String name) {
        this.krName = name;
    }

    public String getKrName()  {
        return krName;
    }
}
