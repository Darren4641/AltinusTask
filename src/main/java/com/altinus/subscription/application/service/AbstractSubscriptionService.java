package com.altinus.subscription.application.service;

import com.altinus.subscription.application.converter.ChannelConverter;
import com.altinus.subscription.application.dto.request.SubscriptionCreateRequestDto;
import com.altinus.subscription.application.dto.request.SubscriptionDeleteRequestDto;
import com.altinus.subscription.application.dto.response.ChannelListResponseDto;
import com.altinus.subscription.application.dto.response.SubscriptionResponseDto;
import com.altinus.subscription.domain.model.Channel;
import com.altinus.subscription.domain.repository.ChannelRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public abstract class AbstractSubscriptionService implements SubscriptionService {
    private final ChannelRepository channelRepository;
    private final ChannelConverter channelConverter;

    @PostConstruct
    public void init() {
        /**
         * 구독 가능 채널: 홈페이지, 모바일앱, 네이버, SKT, KT, LGU+
         * 해지 가능 채널: 홈페이지, 모바일앱, 콜센터, 채팅상담, 이메일
         */

        // 구독, 해지 가능
        saveChannelIfNot("홈페이지", true);
        saveChannelIfNot("모바일앱", true);

        // 구독만 가능
        saveChannelIfNot("네이버", false);
        saveChannelIfNot("SKT", false);
        saveChannelIfNot("KT", false);
        saveChannelIfNot("LGU+", false);

        // 해지만 가능
        saveChannelIfNot("콜센터", true);
        saveChannelIfNot("채팅상담", true);
        saveChannelIfNot("이메일", true);
    }



    /**
     * 채널의 목록을 보여주는 API
     * 공통 메서드이기에 해당 클래스에서 구현
     *
     * @return List<ChannelListResponseDto>
     */
    @Override
    public List<ChannelListResponseDto> findAllChannels() {
        List<Channel> channels = channelRepository.findAllByOrderByCreatedDate();
        return channelConverter.toResponseDtoList(channels);
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


    private void saveChannelIfNot(String name, Boolean type) {
        channelRepository.findByName(name)
                .orElseGet(() -> channelRepository.save(new Channel(name, type)));
    }
}
