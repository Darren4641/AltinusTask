package com.altinus.subscription.application.service;

import com.altinus.subscription.application.dto.request.SubscriptionCreateRequestDto;
import com.altinus.subscription.application.dto.request.SubscriptionDeleteRequestDto;
import com.altinus.subscription.application.dto.response.ChannelListResponseDto;
import com.altinus.subscription.application.dto.response.SubscriptionResponseDto;

import java.util.List;

public interface SubscriptionService {
    List<ChannelListResponseDto> findAllChannels();
    SubscriptionResponseDto subscribe(SubscriptionCreateRequestDto subscriptionCreateRequestDto);
    void deleteSubscription(SubscriptionDeleteRequestDto subscriptionDeleteRequestDto);
}
