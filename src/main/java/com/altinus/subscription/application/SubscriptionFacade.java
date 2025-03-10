package com.altinus.subscription.application;

import com.altinus.subscription.application.dto.response.ChannelListResponseDto;
import com.altinus.subscription.application.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionFacade {
    private final SubscriptionService subscriptionService;

    @Qualifier("premiumSubscriptionService")
    private final SubscriptionService premiumSubscriptionService;

    public List<ChannelListResponseDto> viewAllChannels() {
        return subscriptionService.findAllChannels();
    }
}
