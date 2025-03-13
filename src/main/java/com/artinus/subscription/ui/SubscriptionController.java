package com.artinus.subscription.ui;

import com.artinus.common.response.BaseResponse;
import com.artinus.subscription.application.SubscriptionFacade;
import com.artinus.subscription.application.converter.SubscriptionConverter;
import com.artinus.subscription.application.dto.request.SubscriptionCreateRequestDto;
import com.artinus.subscription.application.dto.request.SubscriptionDeleteRequestDto;
import com.artinus.subscription.application.dto.response.SubscriptionDetailResponseDto;
import com.artinus.subscription.application.dto.response.SubscriptionHistoryDto;
import com.artinus.subscription.application.dto.response.SubscriptionResponseDto;
import com.artinus.subscription.domain.common.SubscriptionStatus;
import com.artinus.subscription.ui.dto.request.PhoneNumberApiDto;
import com.artinus.subscription.ui.dto.request.SubscriptionCreateRequestApiDto;
import com.artinus.subscription.ui.dto.request.SubscriptionDeleteRequestApiDto;
import com.artinus.subscription.ui.dto.response.SubscriptionCreateResponseApiDto;
import com.artinus.subscription.ui.dto.response.SubscriptionDeleteResponseApiDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    /**
     * 나의 현재 구독 중 인 정보
     * Get이아닌 Post로 쓴이유는 url에 phoneNumber를 그대로 노출하기에는 개인정보가 민감해서 Post로 작성
     * @param phoneNumberApiDto
     * @return
     */
    @PostMapping(value = "/info")
    public BaseResponse<Map<SubscriptionStatus, List<SubscriptionDetailResponseDto>>> mySubscription(@RequestBody @Valid PhoneNumberApiDto phoneNumberApiDto) {
        return new BaseResponse<>(subscriptionFacade.getMySubscriptions(phoneNumberApiDto.getPhoneNumber()));
    }

    /**
     * 나의 구독 기록
     * Get이아닌 Post로 쓴이유는 url에 phoneNumber를 그대로 노출하기에는 개인정보가 민감해서 Post로 작성
     * @param phoneNumberApiDto
     * @return
     */
    @PostMapping(value = "/history")
    public BaseResponse<Page<SubscriptionHistoryDto>> mySubscriptionHistory(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                            @RequestParam(value = "size", defaultValue = "10") int size,
                                                                            @RequestBody @Valid PhoneNumberApiDto phoneNumberApiDto) {
        Pageable pageable = PageRequest.of(page, size);
        return new BaseResponse<>(subscriptionFacade.getMySubscriptionHistories(phoneNumberApiDto.getPhoneNumber(), pageable));
    }
}
