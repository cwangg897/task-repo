package com.task.service;

import com.task.controller.response.PaymentApproveResponse;
import com.task.domain.PaymentCreate;
import com.task.controller.response.PaymentCreateResponse;

public interface PaymentStrategy {

    PaymentCreateResponse requestPayment(PaymentCreate request);

    PaymentApproveResponse approvePayment(String paymentKey, String orderId, String amount);
}
