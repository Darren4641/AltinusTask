package com.altinus.subscription.application.converter;

import com.altinus.subscription.application.dto.response.SubscriptionResponseDto;
import com.altinus.subscription.application.dto.response.ValidateSubscriptionResponseDto;
import com.altinus.subscription.domain.model.Channel;
import com.altinus.subscription.domain.model.Subscription;
import com.altinus.subscription.domain.model.enums.SubscriptionStatus;
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
}
