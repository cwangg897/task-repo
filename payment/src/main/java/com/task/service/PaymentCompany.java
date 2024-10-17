package com.task.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentCompany {
    TOSS("tossPaymentService"),
    KAKAO("kakaoPaymentService");
    private final String name;
}
