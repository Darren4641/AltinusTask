package com.artinus.subscription.domain.repository;

import com.artinus.subscription.application.dto.response.SubscriptionDto;
import com.artinus.subscription.domain.model.Subscription;
import com.artinus.subscription.domain.model.enums.SubscriptionStatus;

import java.util.Optional;

public interface SubscriptionDSLRepository {
    Optional<Subscription> findByPhoneNumberAndChannelIdAndStatusDSL(String phoneNumber, Long channelId, SubscriptionStatus status);

    Optional<Subscription> findByPhoneNumberAndChannelIdDSL(String phoneNumber, Long channelId);

    Optional<SubscriptionDto> findSubscriptionDSL(String phoneNumber, Long channelId);
}
