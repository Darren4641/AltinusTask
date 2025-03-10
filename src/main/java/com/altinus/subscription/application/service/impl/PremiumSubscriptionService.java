package com.altinus.subscription.application.service.impl;

import com.altinus.subscription.application.converter.ChannelConverter;
import com.altinus.subscription.application.dto.request.SubscriptionCreateRequestDto;
import com.altinus.subscription.application.dto.request.SubscriptionDeleteRequestDto;
import com.altinus.subscription.application.dto.response.SubscriptionResponseDto;
import com.altinus.subscription.application.service.AbstractSubscriptionService;
import com.altinus.subscription.domain.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("premiumSubscriptionService")
public class PremiumSubscriptionService extends AbstractSubscriptionService {

    public PremiumSubscriptionService(ChannelRepository channelRepository, ChannelConverter channelConverter) {
        super(channelRepository, channelConverter);
    }

    @Override
    public SubscriptionResponseDto subscribe(SubscriptionCreateRequestDto subscriptionCreateRequestDto) {
        return null;
    }

    @Override
    public void deleteSubscription(SubscriptionDeleteRequestDto subscriptionDeleteRequestDto) {

    }
}
