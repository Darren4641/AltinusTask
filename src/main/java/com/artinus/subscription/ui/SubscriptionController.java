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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "구독 API", description = "구독 관련 명세서")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/subscription")
public class SubscriptionController {
    private final SubscriptionConverter subscriptionConverter;
    private final SubscriptionFacade subscriptionFacade;

    /**
     * 채널 구독
     * @param subscriptionCreateRequestApiDto
     * @return
     */
    @Operation(summary = "구독", description = "특정 채널에 구독을 진행합니다. | 구독 안함 -> 구독 | 구독 안함 -> 프리미엄 구독 | 구독  -> 프리미엄 구독")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "구독 성공")
    })
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
    @Operation(summary = "구독 해지", description = "특정 채널에 구독 해지를 진행합니다. | 프리미엄 구독 -> 구독 | 프리미엄 구독 -> 구독 안함 | 구독  -> 구독 안함")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "구독 해지 성공")
    })
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
    @Operation(summary = "나의 구독 정보", description = "현재 입력한 번호의 구독 정보를 구독 형태별로 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "구독 해지 성공")
    })
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
    @Operation(summary = "나의 구독 내역", description = "현재 입력한 번호의 구독 내역을 Paging처리하여 보여줍니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "구독 해지 성공")
    })
    @PostMapping(value = "/history")
    public BaseResponse<Page<SubscriptionHistoryDto>> mySubscriptionHistory(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                            @RequestParam(value = "size", defaultValue = "2") int size,
                                                                            @RequestBody @Valid PhoneNumberApiDto phoneNumberApiDto) {
        Pageable pageable = PageRequest.of(page, size);
        return new BaseResponse<>(subscriptionFacade.getMySubscriptionHistories(phoneNumberApiDto.getPhoneNumber(), pageable));
    }
}
