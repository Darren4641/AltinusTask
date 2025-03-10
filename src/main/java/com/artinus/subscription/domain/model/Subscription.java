package com.artinus.subscription.domain.model;

import com.artinus.common.domain.BaseEntity;
import com.artinus.subscription.domain.model.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@Entity
@Table(
        name = "subscriptions",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_phone_channel", columnNames = {"phoneNumber", "channel_id"})
        },
        indexes = {
                @Index(name = "IDX_subscription_channel", columnList = "channel_id") // FK 인덱스 추가
        }
)
public class Subscription extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
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

    public void subscribeChannel(Channel channel) {
        this.channel = channel;
    }


    public void updateStatus(SubscriptionStatus newStatus) {
        this.status = newStatus;
    }
}