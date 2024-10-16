package com.task.controller;

import com.task.ApiResponse;
import com.task.controller.response.PaymentApproveResponse;
import com.task.domain.PaymentCreate;
import com.task.controller.response.PaymentCreateResponse;
import com.task.service.PaymentApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * reference
 * https://docs.tosspayments.com/guides/payment-widget/integration?frontend=javascript&backend=java
 * https://docs.tosspayments.com/blog/what-is-successurl
 */
@RestControllerAdvice
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentApplicationService paymentApplicationService;

    // 결제 요청 임시 저장위한 요청
    @PostMapping
    public ResponseEntity<ApiResponse<PaymentCreateResponse>> requestPayment(@RequestBody PaymentCreate request){
        PaymentCreateResponse response = paymentApplicationService.requestPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(HttpStatus.CREATED, response));
    }

    /**
     * 결제요청 성공 - 결제 승인 요청
     * /success?paymentType={PAYMENT_TYPE}&orderId={ORDER_ID}
     * &paymentKey={PAYMENT_KEY}&amount={AMOUNT}
     * 쿼리 파라미터의 amount 값과 renderPaymentMethods()의 amount 파라미터의 값이 같은지 반드시 확인하세요.
     * 클라이언트에서 결제 금액을 조작하는 행위를 방지할 수 있습니다.
     * 만약 값이 다르다면 결제를 취소하고 구매자에게 알려주세요.
     */
    @PostMapping("/{pgGateway}/success")
    public ResponseEntity<ApiResponse<PaymentApproveResponse>> approvePayment(
        @PathVariable String pgGateway,
        @RequestParam String paymentKey,
        @RequestParam String orderId,
        @RequestParam String amount
    ){
        PaymentApproveResponse response = paymentApplicationService.approvePayment(
            pgGateway, paymentKey, orderId, amount);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(response));
    }


    /**
     * 결제요청 실패
     * /fail?code={ERROR_CODE}&message={ERROR_MESSAGE}
     * &orderId={ORDER_ID}
     * 실패하면 이렇게 오는데
     * 구매자에 의해 결제가 취소되면 PAY_PROCESS_CANCELED 에러가 발생합니다. 결제 과정이 중단된 것이라서 failUrl로 orderId가 전달되지 않아요.
     * 결제가 실패하면 PAY_PROCESS_ABORTED 에러가 발생합니다.
     * 구매자가 입력한 카드 정보에 문제가 있다면 REJECT_CARD_COMPANY 에러가 발생합니다.
     *
     */
//    public void failRequestPayment(){
//
//    }

}
