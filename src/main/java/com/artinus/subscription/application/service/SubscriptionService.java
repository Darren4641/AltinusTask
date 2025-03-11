package com.artinus.subscription.application.service;

import com.artinus.subscription.application.dto.request.SubscriptionCreateRequestDto;
import com.artinus.subscription.application.dto.request.SubscriptionDeleteRequestDto;
import com.artinus.subscription.application.dto.response.*;
import com.artinus.subscription.domain.model.Channel;
import com.artinus.subscription.domain.model.enums.SubscriptionStatus;

import java.util.List;

public interface SubscriptionService {
    List<ChannelListResponseDto> findAllChannels();

    Boolean callAltinusSubscribe();
    SubscriptionResponseDto subscribe(SubscriptionCreateRequestDto subscriptionCreateRequestDto);
    SubscriptionResponseDto unSubscribe(SubscriptionDeleteRequestDto subscriptionDeleteRequestDto, SubscriptionDto subscriptionDto);

    SubscriptionDto getSubscription(String phoneNumber, Long channelId);


    ValidateSubscriptionResponseDto validateSubscribe(String phoneNumber, Channel channel, SubscriptionStatus newStatus);

    ValidateUnSubscriptionResponseDto validateUnSubscribe(String phoneNumber, Channel channel, SubscriptionStatus newStatus, SubscriptionDto subscriptionDto);

}
