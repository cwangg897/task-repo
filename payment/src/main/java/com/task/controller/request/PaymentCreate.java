package com.task.controller.request;

import com.task.infrastructure.PaymentEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCreate {

    @NotEmpty(message = "orderId는 필수값입니다")
    private String orderId;

    @NotEmpty(message = "userId는 필수값입니다")
    private Long userId;

    @NotEmpty(message = "amount는 필수값입니다")
    @Min(value = 0, message = "0이하 값은 불가능 합니다")
    private Long amount;

    @NotEmpty(message = "payGateway는 필수값입니다")
    private String payGateway;

    @NotEmpty(message = "payType은 필수값입니다")
    private String payType;

    public PaymentEntity toEntity(){
        return new PaymentEntity(amount, orderId, payType, payGateway, userId);
    }
}
