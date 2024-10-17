package com.task.service;

import com.task.controller.response.PaymentApproveResponse;
import com.task.controller.response.PaymentRequestFailResponse;
import com.task.controller.request.PaymentCreate;
import com.task.controller.response.PaymentCreateResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentApplicationService {

    private final Map<String, PaymentStrategy> paymentStrategies;

    // 요청 시
    public PaymentCreateResponse requestPayment(PaymentCreate create) {
        PaymentStrategy paymentStrategy = paymentStrategies.get(create.getPayGateway());
        // 요청 저장
        return paymentStrategy.requestPayment(create);
    }

    // 요청 성공(인증성공) - 승인 요청
    public PaymentApproveResponse approvePayment(String pgGateway, String paymentKey,
        String orderId, String amount) {
        PaymentStrategy paymentStrategy = paymentStrategies.get(pgGateway);
        return paymentStrategy.approvePayment(paymentKey, orderId, amount);
    }

    // ABORTED
    public PaymentRequestFailResponse failPaymentRequest(
        String pagGateway, String code, String message, String orderId) {
        PaymentStrategy paymentStrategy = paymentStrategies.get(pagGateway);
        return paymentStrategy.failPaymentRequest(code, message, orderId);
    }

    // 요청 실패 - 예외 처리
}
