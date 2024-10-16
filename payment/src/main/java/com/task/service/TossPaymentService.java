package com.task.service;

import com.task.ApiException;
import com.task.ErrorType;
import com.task.TossPaymentAcceptRequest;
import com.task.TossPaymentAcceptResponse;
import com.task.controller.response.PaymentApproveResponse;
import com.task.domain.PaymentCreate;
import com.task.controller.response.PaymentCreateResponse;
import com.task.feign.TossClient;
import com.task.infrastructure.payment.PaymentEntity;
import com.task.infrastructure.payment.PaymentRepository;
import com.task.infrastructure.profile.ProfileEntity;
import com.task.infrastructure.profile.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("toss")
@Transactional
@RequiredArgsConstructor
public class TossPaymentService implements PaymentStrategy {

    private final PaymentRepository paymentRepository;
    private final TossClient tossClient;
    private final ProfileRepository profileRepository;

    @Override
    public PaymentCreateResponse requestPayment(PaymentCreate request) {
        PaymentEntity payment = paymentRepository.save(request.toEntity());
        return PaymentCreateResponse.from(payment);
    }

    // todo 예외처리, 상태변경, 포인트 적립(동시성)
    @Override
    public PaymentApproveResponse approvePayment(String paymentKey, String orderId, String amount) {
        PaymentEntity payment = paymentRepository.getByOrderId(orderId)
            .orElseThrow(() -> new ApiException(
                "결제요청 기록을 찾을 수 없습니다",
                ErrorType.NO_RESOURCE, HttpStatus.NOT_FOUND));
        try {
            TossPaymentAcceptResponse response = tossClient.approvePayment(
                new TossPaymentAcceptRequest(orderId, paymentKey, amount));
            // 주문 결제 성공으로 변경
            payment.approveSuccess(response.getStatus(), response.getApprovedAt(), response.getPaymentKey());

            // 포인트 적립
            ProfileEntity profile = profileRepository.getByProfileId(payment.getProfileId())
                .orElseThrow(() ->
                    new ApiException("프로필 id : " + payment.getProfileId() + " 를 찾을 수 없습니다",
                        ErrorType.NO_RESOURCE, HttpStatus.NOT_FOUND));
            profile.addPoint(Long.valueOf(response.getAmount()));
        }catch (ApiException e){

        }







    }
}
