package com.artinus.subscription.ui.dto.request;

import com.artinus.subscription.domain.model.enums.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionCreateRequestApiDto {
    private String phoneNumber;         // 휴대폰번호
    private Long channelId;             // 채널 ID
    private SubscriptionStatus status;  // 구독 상태
}
