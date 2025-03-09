package com.altinus.subscription.application.service;

import com.altinus.subscription.application.dto.request.SubscriptionCreateRequestDto;
import com.altinus.subscription.application.dto.request.SubscriptionDeleteRequestDto;
import com.altinus.subscription.application.dto.response.ChannelListResponseDto;
import com.altinus.subscription.application.dto.response.SubscriptionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public abstract class AbstractSubscriptionService implements SubscriptionService {


    /**
     * 채널의 목록을 보여주는 API
     * 공통 메서드이기에 해당 클래스에서 구현
     *
     * @return List<ChannelListResponseDto>
     */
    @Override
    public List<ChannelListResponseDto> findAllChannels() {
        return Collections.emptyList();
    }

    /**
     * 구독하기 API
     *
     * @param subscriptionCreateRequestDto
     * @return SubscriptionResponseDto
     */
    @Override
    public abstract SubscriptionResponseDto subscribe(SubscriptionCreateRequestDto subscriptionCreateRequestDto);


    /**
     * 구독 취소하기 API
     *
     * @param subscriptionDeleteRequestDto
     */
    @Override
    public abstract void deleteSubscription(SubscriptionDeleteRequestDto subscriptionDeleteRequestDto);
}
