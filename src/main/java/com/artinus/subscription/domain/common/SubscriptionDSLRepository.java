package com.artinus.subscription.domain.common;

import com.artinus.subscription.application.dto.response.SubscriptionDetailResponseDto;
import com.artinus.subscription.application.dto.response.SubscriptionDto;
import com.artinus.subscription.application.dto.response.SubscriptionHistoryDto;
import com.artinus.subscription.domain.subscription.Subscription;
import com.artinus.subscription.domain.common.SubscriptionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SubscriptionDSLRepository {
    Optional<Subscription> findByPhoneNumberAndChannelIdAndStatusDSL(String phoneNumber, Long channelId, SubscriptionStatus status);

    Optional<Subscription> findByPhoneNumberAndChannelIdDSL(String phoneNumber, Long channelId);

    Optional<SubscriptionDto> findSubscriptionDSL(String phoneNumber, Long channelId);

    Map<SubscriptionStatus, List<SubscriptionDetailResponseDto>> findSubscriptionsDetailByPhoneNumberDSL(String phoneNumber);

    Page<SubscriptionHistoryDto> findSubscriptionHistories(String phoneNumber, Pageable pageable);
}
