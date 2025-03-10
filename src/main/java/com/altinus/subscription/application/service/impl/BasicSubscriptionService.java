package com.altinus.subscription.application.service.impl;

import com.altinus.common.exception.ApiErrorException;
import com.altinus.common.response.enums.ResultCode;
import com.altinus.subscription.application.converter.ChannelConverter;
import com.altinus.subscription.application.converter.SubscriptionConverter;
import com.altinus.subscription.application.dto.response.ValidateSubscriptionResponseDto;
import com.altinus.subscription.application.service.AbstractSubscriptionService;
import com.altinus.subscription.domain.model.Channel;
import com.altinus.subscription.domain.model.enums.SubscriptionStatus;
import com.altinus.subscription.domain.repository.ChannelRepository;
import com.altinus.subscription.domain.repository.SubscriptionDSLRepository;
import com.altinus.subscription.domain.repository.SubscriptionHistoryRepository;
import com.altinus.subscription.domain.repository.SubscriptionRepository;
import com.altinus.subscription.infrastructure.external.service.CsrngExternalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Primary
public class BasicSubscriptionService extends AbstractSubscriptionService {


    public BasicSubscriptionService(ChannelRepository channelRepository,
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

    /**
     * [구독 안함 → 일반 구독] 처리
     *
     * @param phoneNumber
     * @param channel
     * @param newStatus
     * @return
     */
    @Override
    public ValidateSubscriptionResponseDto validateSubscribe(String phoneNumber, Channel channel, SubscriptionStatus newStatus) {
        subscriptionDSLRepository
                .findByPhoneNumberAndChannelIdAndStatusDSL(
                        phoneNumber,
                        channel.getId(),
                        newStatus
                )
                .ifPresent(subscription -> {
                    throw new ApiErrorException(ResultCode.ALREADY_REQUEST);
                });


        return subscriptionConverter.toValidateResponseDto(phoneNumber, channel, null, newStatus);
    }
}
