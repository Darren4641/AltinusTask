package com.artinus.subscription.application.dto.response;

import com.artinus.subscription.domain.model.Channel;
import com.artinus.subscription.domain.model.enums.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValidateUnSubscriptionResponseDto {
    private Long id;
    private String phoneNumber;
    private SubscriptionStatus oldStatus;
    private SubscriptionStatus newStatus;
    private Channel channel;
}
