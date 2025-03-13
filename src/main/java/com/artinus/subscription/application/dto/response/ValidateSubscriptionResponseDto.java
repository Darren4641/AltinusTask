package com.artinus.subscription.application.dto.response;

import com.artinus.subscription.domain.channel.Channel;
import com.artinus.subscription.domain.common.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValidateSubscriptionResponseDto {
    private Long id;
    private String phoneNumber;
    private SubscriptionStatus oldStatus;
    private SubscriptionStatus newStatus;
    private Channel channel;
}
