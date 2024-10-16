package com.task.service;

import com.task.ApiException;
import com.task.ErrorType;
import com.task.TossPaymentAcceptRequest;
import com.task.TossPaymentAcceptResponse;
import com.task.controller.response.PaymentApproveResponse;
import com.task.controller.response.PaymentRequestFailResponse;
import com.task.domain.PaymentCreate;
import com.task.controller.response.PaymentCreateResponse;
import com.task.domain.PointAddRequest;
import com.task.feign.TossClient;
import com.task.feign.TossException;
import com.task.feign.UserClient;
import com.task.infrastructure.PaymentEntity;
import com.task.infrastructure.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("toss")
@Transactional
@RequiredArgsConstructor
public class TossPaymentService implements PaymentStrategy {

    private final PaymentRepository paymentRepository;
    private final TossClient tossClient;
    private final UserClient userClient;

    @Value("${toss.success_url}")
    private String successUrl;

    @Value("${toss.fail_url}")
    private String failUrl;

    private final String URL_PREFIX_PG_GATEWAY = "/toss";

    @Override
    public PaymentCreateResponse requestPayment(PaymentCreate request) {
        PaymentEntity payment = paymentRepository.save(request.toEntity());
        return PaymentCreateResponse.of(payment, successUrl+URL_PREFIX_PG_GATEWAY, failUrl+URL_PREFIX_PG_GATEWAY);
    }

    @Override
    public PaymentApproveResponse approvePayment(String paymentKey, String orderId, String amount) {
        PaymentEntity payment = paymentRepository.getByOrderId(orderId)
            .orElseThrow(() -> new ApiException(
                "결제요청 기록을 찾을 수 없습니다",
                ErrorType.NO_RESOURCE, HttpStatus.NOT_FOUND));
        try {
            TossPaymentAcceptResponse response = tossClient.approvePayment(
                new TossPaymentAcceptRequest(orderId, paymentKey, amount));
            // 주문 결제 성공 상태변경
            payment.approveSuccess(response.getStatus(), response.getApprovedAt(),
                response.getPaymentKey());

            // 포인트 적립 - (유저에 저장) feign client로 통신
            // todo 실패하면 로그 남기기 혹은 db에 저장하기 비동기 메소드로 빼기 - 향후 kafka로 변경
            userClient.addPoint(new PointAddRequest(payment.getProfileId(), response.getAmount()));
            return PaymentApproveResponse.from(response);
        } catch (TossException e) {
            // 주문 실패로 상태 업데이트
            payment.approveFail(e.getMessage());
            throw new ApiException(e.getMessage(), ErrorType.EXTERNAL_API_ERROR,
                HttpStatus.valueOf(e.getStatus()));
        }
    }

    @Override
    public PaymentRequestFailResponse failPaymentRequest(String code, String message,
        String orderId) {
        if (orderId != null) {
            PaymentEntity payment = paymentRepository.getByOrderId(orderId)
                .orElseThrow(() -> new ApiException(
                    "결제요청 기록을 찾을 수 없습니다",
                    ErrorType.NO_RESOURCE, HttpStatus.NOT_FOUND));
            payment.failPaymentRequest(message);
        }
        return new PaymentRequestFailResponse(code, message, orderId);
    }
}
