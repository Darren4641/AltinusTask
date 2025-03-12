package com.artinus.subscription.ui.dto.request;

import com.artinus.subscription.domain.model.enums.SubscriptionStatus;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionCreateRequestApiDto {
    @Pattern(regexp = "^010\\d{4}\\d{4}$", message = "휴대폰 번호는 01000000000 4자리-4자리 로 작성해주세요.")
    private String phoneNumber;         // 휴대폰번호
    private Long channelId;             // 채널 ID
    private SubscriptionStatus status;  // 구독 상태
}
