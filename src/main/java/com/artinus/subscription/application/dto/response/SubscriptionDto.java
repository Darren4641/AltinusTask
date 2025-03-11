package com.artinus.subscription.application.dto.response;

import com.artinus.subscription.domain.model.Channel;
import com.artinus.subscription.domain.model.enums.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDto {
    private Long id;
    private String phoneNumber;
    private SubscriptionStatus status;
    private Boolean canUnSubscribe;
    public SubscriptionDto(SubscriptionStatus status) {
        this.status = status;
    }
}
