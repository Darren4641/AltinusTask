package com.artinus.subscription.application.dto.response;

import com.artinus.subscription.domain.common.SubscriptionStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionHistoryDto {
    private Long id;
    private String channelName;
    private SubscriptionStatus oldStatus;
    private SubscriptionStatus newStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedDate;
}
