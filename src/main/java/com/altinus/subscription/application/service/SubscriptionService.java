package com.altinus.subscription.application.service;

import com.altinus.common.exception.ApiErrorException;
import com.altinus.subscription.application.dto.request.SubscriptionCreateRequestDto;
import com.altinus.subscription.application.dto.request.SubscriptionDeleteRequestDto;
import com.altinus.subscription.application.dto.response.ChannelListResponseDto;
import com.altinus.subscription.application.dto.response.SubscriptionResponseDto;
import com.altinus.subscription.application.dto.response.ValidateSubscriptionResponseDto;
import com.altinus.subscription.domain.model.Channel;
import com.altinus.subscription.domain.model.enums.SubscriptionStatus;

import java.util.List;

public interface SubscriptionService {
    List<ChannelListResponseDto> findAllChannels();

    Boolean callAltinusSubscribe();
    SubscriptionResponseDto subscribe(SubscriptionCreateRequestDto subscriptionCreateRequestDto);
    void unSubscription(SubscriptionDeleteRequestDto subscriptionDeleteRequestDto);

    ValidateSubscriptionResponseDto validateSubscribe(String phoneNumber, Channel channel, SubscriptionStatus newStatus);

}
