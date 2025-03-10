package com.artinus.subscription.application.service.impl;

import com.artinus.common.exception.ApiErrorException;
import com.artinus.common.response.enums.ResultCode;
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
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Slf4j
@Service("basicSubscriptionService")
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
        log.info("[Basic 구독]");
        return subscriptionConverter.toValidateResponseDto(null, phoneNumber, channel, null, newStatus);
    }
}
