package com.altinus.subscription.domain.model;

import com.altinus.subscription.domain.model.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String phoneNumber;  // 회원 ID 없이, 휴대폰 번호로 관리

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    public Subscription(String phoneNumber, Channel channel, SubscriptionStatus status) {
        this.phoneNumber = phoneNumber;
        this.channel = channel;
        this.status = status;
    }

    public void updateStatus(SubscriptionStatus newStatus) {
        this.status = newStatus;
    }
}