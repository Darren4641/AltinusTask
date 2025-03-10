package com.altinus.subscription.ui;

import com.altinus.common.response.BaseResponse;
import com.altinus.subscription.application.SubscriptionFacade;
import com.altinus.subscription.application.dto.response.ChannelListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @GetMapping
    public BaseResponse<List<ChannelListResponseDto>> viewAllChannels() {
        List<ChannelListResponseDto> channels = subscriptionFacade.viewAllChannels();
        return new BaseResponse<>(channels);
    }

}
