package com.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TossPaymentAcceptResponse {

    @JsonProperty("paymentKey")
    private String paymentKey;

    @JsonProperty("totalAmount")
    private String amount;

    @JsonProperty("orderId")
    private String orderId;

    @JsonProperty("method")
    private String method;

    @JsonProperty("status")
    private String status;

    @JsonProperty("requestedAt")
    private LocalDateTime requestedAt;

    @JsonProperty("approvedAt")
    private LocalDateTime approvedAt;
}
