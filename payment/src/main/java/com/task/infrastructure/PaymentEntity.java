package com.task.infrastructure;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "payments")
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class PaymentEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id") // toss: payment key
    private String transactionId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "status", nullable = true)
    private String status;

    @Column(name = "pay_type")
    private String payType;

    @Column(name = "pay_gateway")
    private String payGateway;

    @Column(name = "fail_reason")
    private String failReason;

    @CreatedDate
    @Column(updatable = false, name = "created_at", nullable = true)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @Column(name = "approved_at", nullable = false)
    private LocalDateTime approvedAt;

    public void approveSuccess(String status, LocalDateTime approvedAt, String transactionId){
        this.status = status;
        this.approvedAt = approvedAt;
        this.transactionId = transactionId;
    }

    public void approveFail(String failReason){
        this.status = "ABORTED"; // 결제 승인이 실패한 상태
        this.failReason = failReason;
    }

    public PaymentEntity(Long amount, String orderId, String payType, String payGateway, Long userId) {
        this.orderId = orderId;
        this.amount = amount;
        this.payType = payType;
        this.payGateway = payGateway;
        this.userId= userId;
    }

    public PaymentEntity(Long amount, String orderId, String payType, String payGateway, Long userId, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.amount = amount;
        this.payType = payType;
        this.payGateway = payGateway;
        this.userId= userId;
        this.createdAt = createdAt;
    }

    public void failPaymentRequest(String message) {
        this.failReason = message;
        this.status = "ABORTED";
    }
}
