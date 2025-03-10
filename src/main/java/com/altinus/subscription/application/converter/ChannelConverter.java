package com.altinus.subscription.application.converter;

import com.altinus.subscription.application.dto.response.ChannelListResponseDto;
import com.altinus.subscription.application.dto.response.ValidateSubscriptionResponseDto;
import com.altinus.subscription.domain.model.Channel;
import com.altinus.subscription.domain.model.enums.SubscriptionStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChannelConverter {

    private ChannelListResponseDto toResponseDto(Channel channel) {
        return new ChannelListResponseDto(
                channel.getId(),
                channel.getName(),
                channel.getCanUnSubscribe(),
                channel.getCreatedDate()
        );
    }

    /**
     * 채널 목록 반환 DTO
     *
     * @param channels
     * @return
     */
    public List<ChannelListResponseDto> toResponseDtoList(List<Channel> channels) {
        return channels.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

}
