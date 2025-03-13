package com.artinus.subscription.application;

import com.artinus.common.exception.ApiErrorException;
import com.artinus.common.response.enums.ResultCode;
import com.artinus.subscription.application.dto.request.SubscriptionCreateRequestDto;
import com.artinus.subscription.application.dto.request.SubscriptionDeleteRequestDto;
import com.artinus.subscription.application.dto.response.*;
import com.artinus.subscription.application.service.SubscriptionService;
import com.artinus.subscription.domain.common.SubscriptionStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    /**
     * 요청 구독의 상태에 따라 서비스 라우팅
     * 일반 구독 -> subscriptionService
     * 프리미엄 구독 -> premiumSubscriptionService
     * @param subscriptionCreateRequestDto
     * @return
     */
    public SubscriptionResponseDto subscribe(SubscriptionCreateRequestDto subscriptionCreateRequestDto) {
        SubscriptionService targetService = getSubscriptionService(subscriptionCreateRequestDto.getStatus());

        return targetService.subscribe(subscriptionCreateRequestDto);
    }

    /**
     * 현재 구독의 상태에 따라 서비스 라우팅
     * 일반 구독 -> subscriptionService
     * 프리미엄 구독 -> premiumSubscriptionService
     * @param subscriptionDeleteRequestDto
     * @return
     */
    public SubscriptionResponseDto unSubscribe(SubscriptionDeleteRequestDto subscriptionDeleteRequestDto) {
        if(subscriptionDeleteRequestDto.getStatus() == SubscriptionStatus.PREMIUM_SUBSCRIBE) {
            throw new ApiErrorException(ResultCode.ERROR);
        }

        // 해당 전화번호의 현재 구독 상태
        SubscriptionDto subscriptionDto = subscriptionService.getSubscription(subscriptionDeleteRequestDto.getPhoneNumber(), subscriptionDeleteRequestDto.getChannelId());

        SubscriptionService targetService = getSubscriptionService(subscriptionDto.getStatus());

        return targetService.unSubscribe(subscriptionDeleteRequestDto, subscriptionDto);
    }

    public Map<SubscriptionStatus, List<SubscriptionDetailResponseDto>>  getMySubscriptions(String phoneNumber) {
        return subscriptionService.getMySubscriptions(phoneNumber);
    }

    public Page<SubscriptionHistoryDto> getMySubscriptionHistories(String phoneNumber, Pageable pageable) {
        return subscriptionService.getMySubscriptionHistories(phoneNumber, pageable);
    }



    private SubscriptionService getSubscriptionService(SubscriptionStatus status) {
        return switch (status) {
            case SUBSCRIBE -> subscriptionService;
            case PREMIUM_SUBSCRIBE -> premiumSubscriptionService;
            case UNSUBSCRIBE -> throw new ApiErrorException(ResultCode.ERROR);
        };
    }


}
