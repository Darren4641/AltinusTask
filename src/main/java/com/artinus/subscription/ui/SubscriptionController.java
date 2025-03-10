package com.artinus.subscription.ui;

import com.artinus.common.response.BaseResponse;
import com.artinus.subscription.application.SubscriptionFacade;
import com.artinus.subscription.application.converter.SubscriptionConverter;
import com.artinus.subscription.application.dto.request.SubscriptionCreateRequestDto;
import com.artinus.subscription.application.dto.response.SubscriptionResponseDto;
import com.artinus.subscription.ui.dto.request.SubscriptionCreateRequestApiDto;
import com.artinus.subscription.ui.dto.response.SubscriptionCreateResponseApiDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subscription")
public class SubscriptionController {
    private final SubscriptionConverter subscriptionConverter;
    private final SubscriptionFacade subscriptionFacade;

    /**
     * 채널 구독
     * @param subscriptionCreateRequestApiDto
     * @return
     */
    @PostMapping
    public BaseResponse<SubscriptionCreateResponseApiDto> subscribe(@RequestBody @Valid SubscriptionCreateRequestApiDto subscriptionCreateRequestApiDto) {
        SubscriptionCreateRequestDto serviceRequestDto = subscriptionConverter.toCreateServiceDto(subscriptionCreateRequestApiDto);

        SubscriptionResponseDto serviceResponseDto = subscriptionFacade.subscribe(serviceRequestDto);

        return new BaseResponse<>(subscriptionConverter.toCreateResponseDto(serviceResponseDto));
    }
}
