package com.task.controller.response;


import com.task.TossPaymentAcceptResponse;
import com.task.util.DateUtils;
import lombok.Builder;

@Builder
public record PaymentApproveResponse(String amount, String orderId, String method, String status,
                                     String requestedAt, String approvedAt) {


    public static PaymentApproveResponse from(TossPaymentAcceptResponse response){
        return PaymentApproveResponse
            .builder()
            .amount(response.getAmount())
            .orderId(response.getOrderId())
            .method(response.getMethod())
            .status(response.getStatus())
            .requestedAt(DateUtils.formatToKoreaTime(response.getRequestedAt()))
            .approvedAt(DateUtils.formatToKoreaTime(response.getApprovedAt()))
            .build();
    }
}
