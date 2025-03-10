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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
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
        log.info("[Premium 구독]");
        return subscriptionDSLRepository
                .findByPhoneNumberAndChannelIdDSL(phoneNumber, channel.getId())
                .map(subscription -> subscriptionConverter.toValidateResponseDto(
                        subscription.getId(), phoneNumber, channel, subscription.getStatus(), newStatus)
                )
                .orElseGet(() -> subscriptionConverter.toValidateResponseDto(
                        null, phoneNumber, channel, null, newStatus)
                );

    }
}
