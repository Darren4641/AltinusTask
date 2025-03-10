package com.artinus.subscription.domain.repository;

import com.artinus.subscription.domain.model.Subscription;
import com.artinus.subscription.domain.model.enums.SubscriptionStatus;

import java.util.Optional;

public interface SubscriptionDSLRepository {
    Optional<Subscription> findByPhoneNumberAndChannelIdAndStatusDSL(String phoneNumber, Long channelId, SubscriptionStatus status);
}
