package com.artinus.subscription.application.service;

import com.artinus.subscription.application.dto.request.SubscriptionCreateRequestDto;
import com.artinus.subscription.application.dto.request.SubscriptionDeleteRequestDto;
import com.artinus.subscription.application.dto.response.*;
import com.artinus.subscription.domain.channel.Channel;
import com.artinus.subscription.domain.common.SubscriptionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface SubscriptionService {
    List<ChannelListResponseDto> findAllChannels();

    Boolean callAltinusSubscribe();

    void beforeSubscribe();
    SubscriptionResponseDto subscribe(SubscriptionCreateRequestDto subscriptionCreateRequestDto);
    void afterSubscribe();

    void beforeUnSubscribe();
    SubscriptionResponseDto unSubscribe(SubscriptionDeleteRequestDto subscriptionDeleteRequestDto, SubscriptionDto subscriptionDto);
    void afterUnSubscribe();
    SubscriptionDto getSubscription(String phoneNumber, Long channelId);

    ValidateSubscriptionResponseDto validateSubscribe(String phoneNumber, Channel channel, SubscriptionStatus newStatus);

    ValidateUnSubscriptionResponseDto validateUnSubscribe(String phoneNumber, Channel channel, SubscriptionStatus newStatus, SubscriptionDto subscriptionDto);

    Map<SubscriptionStatus, List<SubscriptionDetailResponseDto>> getMySubscriptions(String phoneNumber);

    Page<SubscriptionHistoryDto> getMySubscriptionHistories(String phoneNumber, Pageable pageable);

}
