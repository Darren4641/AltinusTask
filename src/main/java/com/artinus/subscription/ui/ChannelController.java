package com.artinus.subscription.ui;

import com.artinus.common.response.BaseResponse;
import com.artinus.subscription.application.SubscriptionFacade;
import com.artinus.subscription.application.dto.response.ChannelListResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "채널 API", description = "구독 채널 관련 명세서")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/channel")
public class ChannelController {
    private final SubscriptionFacade subscriptionFacade;


    /**
     * 전체 채널 목록 조회 (페이징 X)
     *
     * @return
     */
    @Operation(summary = "채널 조회", description = "현재 DB에 저장된 채널의 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "구독 정보 조회 성공")
    })
    @GetMapping
    public BaseResponse<List<ChannelListResponseDto>> viewAllChannels() {
        List<ChannelListResponseDto> channels = subscriptionFacade.viewAllChannels();
        return new BaseResponse<>(channels);
    }

}
