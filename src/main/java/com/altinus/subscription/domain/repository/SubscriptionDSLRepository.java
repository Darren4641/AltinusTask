package com.altinus.subscription.domain.repository;

import com.altinus.subscription.domain.model.Subscription;
import com.altinus.subscription.domain.model.enums.SubscriptionStatus;

import java.util.Optional;

public interface SubscriptionDSLRepository {
    Optional<Subscription> findByPhoneNumberAndChannelIdAndStatusDSL(String phoneNumber, Long channelId, SubscriptionStatus status);
}
