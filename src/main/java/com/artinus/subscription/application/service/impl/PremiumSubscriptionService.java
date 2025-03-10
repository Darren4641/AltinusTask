package com.artinus.subscription.application.service.impl;

import com.artinus.subscription.application.converter.ChannelConverter;
import com.artinus.subscription.application.converter.SubscriptionConverter;
import com.artinus.subscription.application.dto.response.ValidateSubscriptionResponseDto;
import com.artinus.subscription.application.service.AbstractSubscriptionService;
import com.artinus.subscription.domain.model.Channel;
import com.artinus.subscription.domain.model.enums.SubscriptionStatus;
import com.artinus.subscription.domain.repository.ChannelRepository;
import com.artinus.subscription.domain.repository.SubscriptionDSLRepository;
import com.artinus.subscription.domain.repository.SubscriptionHistoryRepository;
import com.artinus.subscription.domain.repository.SubscriptionRepository;
import com.artinus.subscription.infrastructure.external.service.CsrngExternalService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("premiumSubscriptionService")
public class PremiumSubscriptionService extends AbstractSubscriptionService {

    public PremiumSubscriptionService(ChannelRepository channelRepository,
                                      SubscriptionRepository subscriptionRepository,
                                      SubscriptionHistoryRepository subscriptionHistoryRepository,
                                      SubscriptionDSLRepository subscriptionDSLRepository,
                                      ChannelConverter channelConverter,
                                      SubscriptionConverter subscriptionConverter,
                                      CsrngExternalService csrngExternalService) {
        super(  channelRepository,
                subscriptionRepository,
                subscriptionHistoryRepository,
                subscriptionDSLRepository,
                channelConverter,
                subscriptionConverter,
                csrngExternalService
        );
    }

    @Override
    public ValidateSubscriptionResponseDto validateSubscribe(String phoneNumber, Channel channel, SubscriptionStatus newStatus) {
        return null;
    }
}
