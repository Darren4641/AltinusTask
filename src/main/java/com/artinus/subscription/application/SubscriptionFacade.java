package com.artinus.subscription.application;

import com.artinus.common.exception.ApiErrorException;
import com.artinus.common.response.enums.ResultCode;
import com.artinus.subscription.application.dto.request.SubscriptionCreateRequestDto;
import com.artinus.subscription.application.dto.request.SubscriptionDeleteRequestDto;
import com.artinus.subscription.application.dto.response.ChannelListResponseDto;
import com.artinus.subscription.application.dto.response.SubscriptionResponseDto;
import com.artinus.subscription.application.service.SubscriptionService;
import com.artinus.subscription.domain.model.enums.SubscriptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionFacade {
    private final SubscriptionService subscriptionService;
    private final SubscriptionService premiumSubscriptionService;
    public SubscriptionFacade(
            @Qualifier("basicSubscriptionService") SubscriptionService subscriptionService,
            @Qualifier("premiumSubscriptionService") SubscriptionService premiumSubscriptionService
    ) {
        this.subscriptionService = subscriptionService;
        this.premiumSubscriptionService = premiumSubscriptionService;
    }
    

    public List<ChannelListResponseDto> viewAllChannels() {
        return subscriptionService.findAllChannels();
    }

    public SubscriptionResponseDto subscribe(SubscriptionCreateRequestDto subscriptionCreateRequestDto) {
        SubscriptionService targetService = getSubscriptionService(subscriptionCreateRequestDto.getStatus());

        return targetService.subscribe(subscriptionCreateRequestDto);
    }

    public SubscriptionResponseDto unSubscribe(SubscriptionDeleteRequestDto subscriptionDeleteRequestDto) {
        return null;
    }

    private SubscriptionService getSubscriptionService(SubscriptionStatus status) {
        return switch (status) {
            case SUBSCRIBE -> subscriptionService;
            case PREMIUM_SUBSCRIBE -> premiumSubscriptionService;
            case UNSUBSCRIBE -> throw new ApiErrorException(ResultCode.ERROR);
        };
    }

}
