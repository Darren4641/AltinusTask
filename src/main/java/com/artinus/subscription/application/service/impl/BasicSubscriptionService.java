package com.artinus.subscription.application.service.impl;

import com.artinus.common.exception.ApiErrorException;
import com.artinus.common.response.enums.ResultCode;
import com.artinus.subscription.application.converter.ChannelConverter;
import com.artinus.subscription.application.converter.SubscriptionConverter;
import com.artinus.subscription.application.dto.response.SubscriptionDto;
import com.artinus.subscription.application.dto.response.ValidateSubscriptionResponseDto;
import com.artinus.subscription.application.dto.response.ValidateUnSubscriptionResponseDto;
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
     * ValidateSubscriptionResponseDto 기준으로 Subscription, History 저장 또는 변경
     * @param phoneNumber   구독자 전화번호
     * @param channel       구독할 채널
     * @param newStatus     변경할 구독 상태
     * @return
     * ValidateSubscriptionResponseDto.class
     * id: 수정할 SubscriptionId, null일 경우 새로 생성
     * phoneNumber: 구독자 전화번호
     * oldStatus: 기존 구독 상태 (신규 구독시 UNSUBSCRIBE 고정)
     * newStatus: 변경할 구독 상태
     * channel: 구독할 채널
     */
    @Override
    public ValidateSubscriptionResponseDto validateSubscribe(String phoneNumber, Channel channel, SubscriptionStatus newStatus) {
        log.info("[Basic 구독]");
        return subscriptionConverter.toValidateSubResponseDto(null, phoneNumber, channel, SubscriptionStatus.UNSUBSCRIBE, newStatus);
    }

    /**
     * ValidateSubscriptionResponseDto 기준으로 Subscription, History 삭제 또는 변경
     * @param phoneNumber   구독자 전화번호
     * @param channel       구독할 채널
     * @param newStatus     변경할 구독 상태
     * @return
     * ValidateSubscriptionResponseDto.class
     * id: 수정 및 삭제할 SubscriptionId
     * phoneNumber: 구독자 전화번호
     * oldStatus: 기존 구독 상태
     * newStatus: 변경할 구독 상태
     * channel: 구독할 채널
     */
    @Override
    public ValidateUnSubscriptionResponseDto validateUnSubscribe(String phoneNumber, Channel channel, SubscriptionStatus newStatus, SubscriptionDto subscriptionDto) {
        log.info("[Basic 구독 해지]");
        return subscriptionConverter.toValidateUnSubResponseDto(subscriptionDto.getId(), phoneNumber, channel, subscriptionDto.getStatus(), newStatus);
    }
}
