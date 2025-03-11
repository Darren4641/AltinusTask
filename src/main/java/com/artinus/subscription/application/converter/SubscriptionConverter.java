package com.artinus.subscription.application.converter;

import com.artinus.subscription.application.dto.request.SubscriptionCreateRequestDto;
import com.artinus.subscription.application.dto.request.SubscriptionDeleteRequestDto;
import com.artinus.subscription.application.dto.response.SubscriptionDto;
import com.artinus.subscription.application.dto.response.SubscriptionResponseDto;
import com.artinus.subscription.application.dto.response.ValidateSubscriptionResponseDto;
import com.artinus.subscription.application.dto.response.ValidateUnSubscriptionResponseDto;
import com.artinus.subscription.domain.model.Channel;
import com.artinus.subscription.domain.model.Subscription;
import com.artinus.subscription.domain.model.enums.SubscriptionStatus;
import com.artinus.subscription.ui.dto.request.SubscriptionCreateRequestApiDto;
import com.artinus.subscription.ui.dto.request.SubscriptionDeleteRequestApiDto;
import com.artinus.subscription.ui.dto.response.SubscriptionCreateResponseApiDto;
import com.artinus.subscription.ui.dto.response.SubscriptionDeleteResponseApiDto;
import org.springframework.stereotype.Component;


@Component
public class SubscriptionConverter {


    /**
     *
     * @param id JPA의 더티체킹을 통해 null -> 새로 생성
     * @param phoneNumber
     * @param channel
     * @param oldStatus
     * @param newStatus
     * @return
     */
    public ValidateSubscriptionResponseDto toValidateSubResponseDto(Long id, String phoneNumber, Channel channel, SubscriptionStatus oldStatus, SubscriptionStatus newStatus) {
        return new ValidateSubscriptionResponseDto(
                id,
                phoneNumber,
                oldStatus,
                newStatus,
                channel
        );
    }

    public ValidateUnSubscriptionResponseDto toValidateUnSubResponseDto(Long id, String phoneNumber, Channel channel, SubscriptionStatus oldStatus, SubscriptionStatus newStatus) {
        return new ValidateUnSubscriptionResponseDto(
                id,
                phoneNumber,
                oldStatus,
                newStatus,
                channel
        );
    }

    public SubscriptionResponseDto toSubscriptionResponseDto(Subscription subscriptionEntity, Channel channel) {
        return new SubscriptionResponseDto(
                subscriptionEntity.getId(),
                channel.getName(),
                subscriptionEntity.getStatus()
        );
    }

    public SubscriptionCreateRequestDto toCreateServiceDto(SubscriptionCreateRequestApiDto subscriptionCreateRequestApiDto) {
        return new SubscriptionCreateRequestDto(
                subscriptionCreateRequestApiDto.getPhoneNumber(),
                subscriptionCreateRequestApiDto.getChannelId(),
                subscriptionCreateRequestApiDto.getStatus()
        );
    }

    public SubscriptionCreateResponseApiDto toCreateResponseDto(SubscriptionResponseDto subscriptionResponseDto) {
        return new SubscriptionCreateResponseApiDto(
                subscriptionResponseDto.getChannelId(),
                subscriptionResponseDto.getChannelName(),
                subscriptionResponseDto.getStatus()
        );
    }

    public SubscriptionDeleteRequestDto toDeleteServiceDto(SubscriptionDeleteRequestApiDto subscriptionDeleteRequestApiDto) {
        return new SubscriptionDeleteRequestDto(
                subscriptionDeleteRequestApiDto.getPhoneNumber(),
                subscriptionDeleteRequestApiDto.getChannelId(),
                subscriptionDeleteRequestApiDto.getStatus()
        );
    }

    public SubscriptionDeleteResponseApiDto toDeleteResponseDto(SubscriptionResponseDto subscriptionResponseDto) {
        return new SubscriptionDeleteResponseApiDto(
                subscriptionResponseDto.getChannelId(),
                subscriptionResponseDto.getChannelName(),
                subscriptionResponseDto.getStatus()
        );
    }
}
