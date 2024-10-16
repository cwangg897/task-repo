package com.task.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class PaymentApproveResponse {

    private String amount;
    private String orderId;
    private String method;
    private String status;
    private String requestedAt;
    private String approvedAt;
}
