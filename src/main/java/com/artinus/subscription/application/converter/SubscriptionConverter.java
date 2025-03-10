package com.artinus.subscription.application.converter;

import com.artinus.subscription.application.dto.request.SubscriptionCreateRequestDto;
import com.artinus.subscription.application.dto.response.SubscriptionResponseDto;
import com.artinus.subscription.application.dto.response.ValidateSubscriptionResponseDto;
import com.artinus.subscription.domain.model.Channel;
import com.artinus.subscription.domain.model.Subscription;
import com.artinus.subscription.domain.model.enums.SubscriptionStatus;
import com.artinus.subscription.ui.dto.request.SubscriptionCreateRequestApiDto;
import com.artinus.subscription.ui.dto.response.SubscriptionCreateResponseApiDto;
import org.springframework.stereotype.Component;


@Component
public class SubscriptionConverter {


    public ValidateSubscriptionResponseDto toValidateResponseDto(String phoneNumber, Channel channel, SubscriptionStatus oldStatus, SubscriptionStatus newStatus) {
        return new ValidateSubscriptionResponseDto(
                phoneNumber,
                oldStatus,
                newStatus,
                channel
        );
    }

    public SubscriptionResponseDto toSubscriptionResponseDto(Subscription newSubscription, Channel channel) {
        return new SubscriptionResponseDto(
                newSubscription.getId(),
                channel.getName(),
                newSubscription.getStatus()
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
}
