package com.altinus.subscription.application.dto.response;

import com.altinus.subscription.domain.model.Channel;
import com.altinus.subscription.domain.model.enums.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValidateSubscriptionResponseDto {
    private String phoneNumber;
    private SubscriptionStatus oldStatus;
    private SubscriptionStatus newStatus;
    private Channel channel;
}
