package com.artinus.subscription.application.dto.response;

import com.artinus.subscription.domain.model.enums.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponseDto {
    private Long channelId;
    private String channelName;
    private SubscriptionStatus status;
}
