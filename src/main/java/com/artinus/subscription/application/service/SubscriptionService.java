package com.artinus.subscription.application.service;

import com.artinus.subscription.application.dto.request.SubscriptionCreateRequestDto;
import com.artinus.subscription.application.dto.request.SubscriptionDeleteRequestDto;
import com.artinus.subscription.application.dto.response.ChannelListResponseDto;
import com.artinus.subscription.application.dto.response.SubscriptionResponseDto;
import com.artinus.subscription.application.dto.response.ValidateSubscriptionResponseDto;
import com.artinus.subscription.domain.model.Channel;
import com.artinus.subscription.domain.model.enums.SubscriptionStatus;

import java.util.List;

public interface SubscriptionService {
    List<ChannelListResponseDto> findAllChannels();

    Boolean callAltinusSubscribe();
    SubscriptionResponseDto subscribe(SubscriptionCreateRequestDto subscriptionCreateRequestDto);
    void unSubscription(SubscriptionDeleteRequestDto subscriptionDeleteRequestDto);

    ValidateSubscriptionResponseDto validateSubscribe(String phoneNumber, Channel channel, SubscriptionStatus newStatus);

}
