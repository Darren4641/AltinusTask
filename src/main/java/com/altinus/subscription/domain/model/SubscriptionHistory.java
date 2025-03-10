package com.altinus.subscription.domain.model;

import com.altinus.common.domain.BaseEntity;
import com.altinus.subscription.domain.model.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subscription_history")
public class SubscriptionHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String phoneNumber; // 회원 ID 없이, 휴대폰 번호로 관리

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus newStatus;

    @Column(nullable = false)
    private LocalDateTime changedAt;

    @Column(nullable = false)
    private String channelName;

    public SubscriptionHistory(String phoneNumber, SubscriptionStatus oldStatus, SubscriptionStatus newStatus, String channelName) {
        this.phoneNumber = phoneNumber;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.channelName = channelName;
        this.changedAt = LocalDateTime.now();
    }
}