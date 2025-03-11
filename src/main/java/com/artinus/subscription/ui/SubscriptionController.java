package com.artinus.subscription.ui;

import com.artinus.common.response.BaseResponse;
import com.artinus.subscription.application.SubscriptionFacade;
import com.artinus.subscription.application.converter.SubscriptionConverter;
import com.artinus.subscription.application.dto.request.SubscriptionCreateRequestDto;
import com.artinus.subscription.application.dto.request.SubscriptionDeleteRequestDto;
import com.artinus.subscription.application.dto.response.SubscriptionResponseDto;
import com.artinus.subscription.ui.dto.request.SubscriptionCreateRequestApiDto;
import com.artinus.subscription.ui.dto.request.SubscriptionDeleteRequestApiDto;
import com.artinus.subscription.ui.dto.response.SubscriptionCreateResponseApiDto;
import com.artinus.subscription.ui.dto.response.SubscriptionDeleteResponseApiDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 채널 구독 해지
     * @param subscriptionDeleteRequestApiDto
     * @return
     */
    @DeleteMapping
    public BaseResponse<SubscriptionDeleteResponseApiDto> unSubscribe(@RequestBody @Valid SubscriptionDeleteRequestApiDto subscriptionDeleteRequestApiDto) {
        SubscriptionDeleteRequestDto serviceRequestDto = subscriptionConverter.toDeleteServiceDto(subscriptionDeleteRequestApiDto);

        SubscriptionResponseDto serviceResponseDto = subscriptionFacade.unSubscribe(serviceRequestDto);

        return new BaseResponse<>(subscriptionConverter.toDeleteResponseDto(serviceResponseDto));
    }
}
