package com.task.controller.response;

import com.task.infrastructure.PaymentEntity;
import com.task.util.DateUtils;
import lombok.Builder;


@Builder
public record PaymentCreateResponse(String orderId, Long amount, String payGateway, String payType, String createdAt, Long userId,
                                    String successUrl, String failUrl){

    public static PaymentCreateResponse of(PaymentEntity payment, String successUrl,
        String failUrl) {
        return PaymentCreateResponse.builder()
            .orderId(payment.getOrderId())
            .amount(payment.getAmount())
            .payGateway(payment.getPayGateway())
            .payType(payment.getPayType())
            .userId(payment.getUserId())
            .successUrl(successUrl)
            .failUrl(failUrl)
            .createdAt(DateUtils.formatToKoreaTime(payment.getCreatedAt()))
            .build();
    }
}
