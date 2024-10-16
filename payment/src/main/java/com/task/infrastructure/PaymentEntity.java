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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "payments")
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id") // toss: payment key
    private String transactionId;

    @Column(name = "profile_id")
    private String profileId;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "status")
    private String status;

    @Column(name = "pay_type")
    private String payType;

    @Column(name = "pay_gateway")
    private String payGateway;

    @Column(name = "fail_reason")
    private String failReason;

    @CreatedDate
    @Column(updatable = false, name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
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

    public PaymentEntity(Long amount, String orderId, String payType, String payGateway, String profileId) {
        this.orderId = orderId;
        this.amount = amount;
        this.payType = payType;
        this.payGateway = payGateway;
        this.profileId= profileId;
    }

    public void failPaymentRequest(String message) {
        this.failReason = message;
        this.status = "ABORTED";
    }
}
