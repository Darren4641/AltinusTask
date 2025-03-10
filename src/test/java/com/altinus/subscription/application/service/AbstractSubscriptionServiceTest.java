package com.altinus.subscription.application.service;

import com.altinus.subscription.application.converter.ChannelConverter;
import com.altinus.subscription.application.converter.SubscriptionConverter;
import com.altinus.subscription.application.dto.request.SubscriptionCreateRequestDto;
import com.altinus.subscription.application.dto.response.SubscriptionResponseDto;
import com.altinus.subscription.application.dto.response.ValidateSubscriptionResponseDto;
import com.altinus.subscription.application.service.impl.BasicSubscriptionService;
import com.altinus.subscription.domain.model.Channel;
import com.altinus.subscription.domain.model.Subscription;
import com.altinus.subscription.domain.model.SubscriptionHistory;
import com.altinus.subscription.domain.model.enums.SubscriptionStatus;
import com.altinus.subscription.domain.repository.ChannelRepository;
import com.altinus.subscription.domain.repository.SubscriptionDSLRepository;
import com.altinus.subscription.domain.repository.SubscriptionHistoryRepository;
import com.altinus.subscription.domain.repository.SubscriptionRepository;
import com.altinus.subscription.infrastructure.external.dto.CsrngResponseDto;
import com.altinus.subscription.infrastructure.external.service.CsrngExternalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AbstractSubscriptionServiceTest {

    @Mock
    protected ChannelRepository channelRepository;
    @Mock
    protected SubscriptionRepository subscriptionRepository;
    @Mock
    protected SubscriptionHistoryRepository subscriptionHistoryRepository;
    @Mock
    protected SubscriptionDSLRepository subscriptionDSLRepository;
    @Mock
    protected SubscriptionConverter subscriptionConverter;
    @Mock
    protected CsrngExternalService csrngExternalService;

    @InjectMocks
    private BasicSubscriptionService basicSubscriptionService;

    private Channel testChannel;
    private String testPhoneNumber;
    private SubscriptionStatus testStatus;

    @BeforeEach
    void setUp() {
        testChannel = new Channel(1L, "Test Channel", true);
        channelRepository.save(testChannel);
        testPhoneNumber = "01012345678";
    }

    @Test
    void 일반_구독_성공() {
        // Given: Request DTO 생성
        SubscriptionCreateRequestDto testRequestDto = new SubscriptionCreateRequestDto(testPhoneNumber, testChannel.getId(), SubscriptionStatus.SUBSCRIBE);

        // 1. 채널 DB 조회 스텁
        when(channelRepository.findById(testRequestDto.getChannelId()))
                .thenReturn(Optional.of(testChannel));

        // 2. 구독 여부 확인 스텁 (validateSubscribe 결과)
        ValidateSubscriptionResponseDto validateResponse =
                new ValidateSubscriptionResponseDto(testPhoneNumber, null, SubscriptionStatus.SUBSCRIBE, testChannel);
        when(subscriptionConverter.toValidateResponseDto(testPhoneNumber, testChannel, null, SubscriptionStatus.SUBSCRIBE))
                .thenReturn(validateResponse);

        // 5. 외부 API 호출 스텁 (랜덤값이 1이면 정상)
        when(csrngExternalService.fetchRandomData())
                .thenReturn(List.of(new CsrngResponseDto("success", 0, 1, 1)));

        // Repository의 save() 메서드는 호출 시 전달된 인자를 그대로 반환하도록 스텁
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(subscriptionHistoryRepository.save(any(SubscriptionHistory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 6. 최종 응답 변환 스텁
        SubscriptionResponseDto expectedResponse = new SubscriptionResponseDto(
                testChannel.getId(),
                testChannel.getName(),
                SubscriptionStatus.SUBSCRIBE
        );
        when(subscriptionConverter.toSubscriptionResponseDto(any(Subscription.class), eq(testChannel)))
                .thenReturn(expectedResponse);

        // When: 구독 메서드 호출
        SubscriptionResponseDto response = basicSubscriptionService.subscribe(testRequestDto);

        // Then: 응답 검증
        assertThat(response).isNotNull();
        assertThat(response.getChannelId()).isEqualTo(testRequestDto.getChannelId());
        assertThat(response.getChannelName()).isEqualTo(testChannel.getName());
        assertThat(response.getStatus()).isEqualTo(SubscriptionStatus.SUBSCRIBE);

        // 추가: 각 저장 메서드의 호출 여부 검증
        verify(channelRepository, times(1)).findById(testRequestDto.getChannelId());
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
        verify(subscriptionHistoryRepository, times(1)).save(any(SubscriptionHistory.class));
        verify(csrngExternalService, times(1)).fetchRandomData();
        verify(subscriptionConverter, times(1)).toSubscriptionResponseDto(any(Subscription.class), eq(testChannel));
    }


}