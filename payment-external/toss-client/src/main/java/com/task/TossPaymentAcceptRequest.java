package com.task;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TossPaymentAcceptRequest {
    private String orderId;
    private String paymentKey;
    private String amount;
}
