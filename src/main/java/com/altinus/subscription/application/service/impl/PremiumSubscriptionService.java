package com.altinus.subscription.application.service.impl;

import com.altinus.subscription.application.converter.ChannelConverter;
import com.altinus.subscription.application.converter.SubscriptionConverter;
import com.altinus.subscription.application.dto.request.SubscriptionCreateRequestDto;
import com.altinus.subscription.application.dto.request.SubscriptionDeleteRequestDto;
import com.altinus.subscription.application.dto.response.SubscriptionResponseDto;
import com.altinus.subscription.application.dto.response.ValidateSubscriptionResponseDto;
import com.altinus.subscription.application.service.AbstractSubscriptionService;
import com.altinus.subscription.domain.model.Channel;
import com.altinus.subscription.domain.model.enums.SubscriptionStatus;
import com.altinus.subscription.domain.repository.ChannelRepository;
import com.altinus.subscription.domain.repository.SubscriptionDSLRepository;
import com.altinus.subscription.domain.repository.SubscriptionHistoryRepository;
import com.altinus.subscription.domain.repository.SubscriptionRepository;
import com.altinus.subscription.infrastructure.external.service.CsrngExternalService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
