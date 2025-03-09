package com.altinus.subscription.application.service.impl;

import com.altinus.subscription.application.dto.request.SubscriptionCreateRequestDto;
import com.altinus.subscription.application.dto.request.SubscriptionDeleteRequestDto;
import com.altinus.subscription.application.dto.response.SubscriptionResponseDto;
import com.altinus.subscription.application.service.AbstractSubscriptionService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class BasicSubscriptionService extends AbstractSubscriptionService {


    @Override
    public SubscriptionResponseDto subscribe(SubscriptionCreateRequestDto subscriptionCreateRequestDto) {
        return null;
    }

    @Override
    public void deleteSubscription(SubscriptionDeleteRequestDto subscriptionDeleteRequestDto) {

    }
}
