package com.task.controller.response;

import com.task.infrastructure.PaymentEntity;
import com.task.util.DateUtils;
import lombok.Builder;


@Builder
public record PaymentCreateResponse(String orderId, Long amount, String payGateway, String payType, String createdAt, String userId){

    public static PaymentCreateResponse from(PaymentEntity payment) {
        return PaymentCreateResponse.builder()
            .orderId(payment.getOrderId())
            .amount(payment.getAmount())
            .payGateway(payment.getPayGateway())
            .payType(payment.getPayType())
            .userId(payment.getProfileId())
            .createdAt(DateUtils.formatToKoreaTime(payment.getCreatedAt()))
            .build();
    }
}
