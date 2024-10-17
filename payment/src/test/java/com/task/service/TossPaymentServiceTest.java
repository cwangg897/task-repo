package com.task.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.task.ApiException;
import com.task.TossPaymentAcceptRequest;
import com.task.TossPaymentAcceptResponse;
import com.task.controller.request.PaymentCreate;
import com.task.feign.TossClient;
import com.task.feign.TossException;
import com.task.feign.UserClient;
import com.task.infrastructure.PaymentEntity;
import com.task.infrastructure.PaymentRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class TossPaymentServiceTest {

    @Autowired
    TossPaymentService tossPaymentService;

    @MockBean
    PaymentRepository paymentRepository;

    @MockBean
    TossClient tossClient;

    @MockBean
    UserClient userClient;


    @Test
    public void 결제요청을받으면_결제요청기록을저장한다() throws Exception {
        // given
        String orderId = "testOrderId";
        Long userId = 1L;
        Long amount = 10000L;
        PaymentCompany payGateway = PaymentCompany.TOSS;
        String payType = "card";
        PaymentCreate request = new PaymentCreate(orderId, userId, amount, payGateway, payType);
        PaymentEntity entity = new PaymentEntity(amount, orderId, payType, payGateway.getName(), userId,
            LocalDateTime.now());
        when(paymentRepository.save(any())).thenReturn(entity);
        // when
        tossPaymentService.requestPayment(request);
        // then
        verify(paymentRepository, times(1)).save(any());
    }


    @Test
    public void 결제_승인_성공() throws Exception {
        // given
        String paymentKey = "testPaymentKey";
        String orderId = "orderId";
        String amount = "1000";
        when(paymentRepository.getByOrderId(any()))
            .thenReturn(
                Optional.of(new PaymentEntity(Long.valueOf(amount), orderId, "card", "toss", 1L)));
        TossPaymentAcceptResponse response = TossPaymentAcceptResponse.builder()
            .paymentKey(paymentKey)
            .amount(amount)
            .orderId(orderId)
            .method("card")
            .status("DONE")
            .requestedAt(LocalDateTime.now())
            .approvedAt(LocalDateTime.now())
            .build();

        when(tossClient.approvePayment(any())).thenReturn(response);
        // when
        tossPaymentService.approvePayment(paymentKey, orderId, amount);
        // then
        verify(paymentRepository, times(1)).getByOrderId(any());
        verify(tossClient, times(1)).approvePayment(any());
        verify(userClient, times(1)).addPoint(any());
    }

    @Test
    public void 결제_승인_실패() throws Exception {
        // given
        String paymentKey = "testPaymentKey";
        String orderId = "orderId";
        String amount = "1000";
        when(paymentRepository.getByOrderId(any()))
            .thenReturn(
                Optional.of(new PaymentEntity(Long.valueOf(amount), orderId, "card", "toss", 1L)));
        when(tossClient.approvePayment(any())).thenThrow(new TossException("failCode", "failMessage", 400));
        // when
        assertThrows(ApiException.class, () ->
            tossPaymentService.approvePayment(paymentKey, orderId, amount)
        );
        // then
        verify(paymentRepository, times(1)).getByOrderId(any());
        verify(tossClient, times(1)).approvePayment(any());
    }

    @Test
    public void 결제요청실패시에_실패URL로가서_요청상태를변경한다_orderId가_null이아닌경우() throws Exception {
        // given
        String code = "testFailCode";
        String message = "testFailMessage";
        String orderId = "testOrderId";
        when(paymentRepository.getByOrderId(orderId))
            .thenReturn(Optional.of(new PaymentEntity(1000L, orderId, "card", "toss", 1L)));
        // when
        tossPaymentService.failPaymentRequest(code, message, orderId);
        // then
        verify(paymentRepository, times(1)).getByOrderId(any());
    }

    @Test
    public void 결제요청실패시에_실패URL로가서_요청상태를변경한다_orderId가_null인경우() throws Exception {
        // given
        String code = "testFailCode";
        String message = "testFailMessage";
        // when
        tossPaymentService.failPaymentRequest(code, message, null);
        // then
        verify(paymentRepository, times(0)).getByOrderId(any());
    }



}