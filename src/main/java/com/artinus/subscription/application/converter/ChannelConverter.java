package com.artinus.subscription.application.converter;

import com.artinus.subscription.application.dto.response.ChannelListResponseDto;
import com.artinus.subscription.domain.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChannelConverter {

    private ChannelListResponseDto toResponseDto(Channel channel) {
        return new ChannelListResponseDto(
                channel.getId(),
                channel.getName(),
                channel.getCanSubscribe(),
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
