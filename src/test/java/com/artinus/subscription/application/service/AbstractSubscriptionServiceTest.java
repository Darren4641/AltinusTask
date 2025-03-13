package com.artinus.subscription.application.service;

import com.artinus.subscription.application.converter.SubscriptionConverter;
import com.artinus.subscription.application.dto.request.SubscriptionCreateRequestDto;
import com.artinus.subscription.application.dto.request.SubscriptionDeleteRequestDto;
import com.artinus.subscription.application.dto.response.SubscriptionDto;
import com.artinus.subscription.application.dto.response.SubscriptionResponseDto;
import com.artinus.subscription.application.dto.response.ValidateSubscriptionResponseDto;
import com.artinus.subscription.application.dto.response.ValidateUnSubscriptionResponseDto;
import com.artinus.subscription.application.service.impl.BasicSubscriptionService;
import com.artinus.subscription.application.service.impl.PremiumSubscriptionService;
import com.artinus.subscription.domain.channel.Channel;
import com.artinus.subscription.domain.subscription.Subscription;
import com.artinus.subscription.domain.history.SubscriptionHistory;
import com.artinus.subscription.domain.common.SubscriptionStatus;
import com.artinus.subscription.domain.channel.ChannelRepository;
import com.artinus.subscription.domain.common.SubscriptionDSLRepository;
import com.artinus.subscription.domain.history.SubscriptionHistoryRepository;
import com.artinus.subscription.domain.subscription.SubscriptionRepository;
import com.artinus.subscription.infrastructure.external.dto.CsrngResponseDto;
import com.artinus.subscription.infrastructure.external.service.CsrngExternalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbstractSubscriptionServiceTest {

    @Mock
    protected ChannelRepository channelRepository;
    @Mock
    protected SubscriptionRepository subscriptionRepository;
    @Mock
    protected SubscriptionHistoryRepository subscriptionHistoryRepository;
    @Mock
    protected SubscriptionConverter subscriptionConverter;
    @Mock
    protected CsrngExternalService csrngExternalService;

    @Mock
    protected SubscriptionDSLRepository subscriptionDSLRepository;

    @InjectMocks
    private BasicSubscriptionService basicSubscriptionService;

    @InjectMocks
    private PremiumSubscriptionService premiumSubscriptionService;

    private Channel testChannel;
    private String testPhoneNumber;

    @BeforeEach
    void setUp() {
        testChannel = new Channel(1L, "Test Channel", true, true);
        channelRepository.save(testChannel);
        testPhoneNumber = "01012345678";
    }

    @Test
    void 일반_구독_성공() {
        // Given: Request DTO 생성
        SubscriptionCreateRequestDto testRequestDto = new SubscriptionCreateRequestDto(testPhoneNumber, testChannel.getId(), SubscriptionStatus.SUBSCRIBE);

        // 1. 채널 DB 조회
        when(channelRepository.findById(testRequestDto.getChannelId()))
                .thenReturn(Optional.of(testChannel));

        // 2. 구독 여부 확인 (validateSubscribe 결과)
        ValidateSubscriptionResponseDto validateResponse =
                new ValidateSubscriptionResponseDto(null, testPhoneNumber, null, SubscriptionStatus.SUBSCRIBE, testChannel);
        when(subscriptionConverter.toValidateSubResponseDto(null, testPhoneNumber, testChannel, SubscriptionStatus.UNSUBSCRIBE, SubscriptionStatus.SUBSCRIBE))
                .thenReturn(validateResponse);

        // 5. 외부 API 호출 (강제 1 주입)
        when(csrngExternalService.fetchRandomData())
                .thenReturn(List.of(new CsrngResponseDto("success", 0, 1, 1)));

        // Repository의 save() 메서드는 호출 시 전달된 인자를 그대로 반환
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

    @Test
    void 프리미엄_구독_업데이트() {
        // Given
        SubscriptionStatus newStatus = SubscriptionStatus.PREMIUM_SUBSCRIBE;
        SubscriptionStatus existingStatus = SubscriptionStatus.SUBSCRIBE;

        when(channelRepository.findById(testChannel.getId()))
                .thenReturn(Optional.of(testChannel));

        Subscription existingSubscription = new Subscription(1L, testPhoneNumber, existingStatus, testChannel);


        // Given 1. 기존 구독이 존재하는 경우 (Optional.of(existingSubscription) 반환)
        when(subscriptionDSLRepository.findByPhoneNumberAndChannelIdDSL(testPhoneNumber, testChannel.getId()))
                .thenReturn(Optional.of(existingSubscription));

        // Given 2. 기존 구독을 기반으로 ValidateSubscriptionResponseDto 변환
        ValidateSubscriptionResponseDto validateResponse =
                new ValidateSubscriptionResponseDto(existingSubscription.getId(), testPhoneNumber, existingStatus, newStatus, testChannel);

        // 5. 외부 API 호출 (강제 1 주입)
        when(csrngExternalService.fetchRandomData())
                .thenReturn(List.of(new CsrngResponseDto("success", 0, 1, 1)));

        when(subscriptionConverter.toValidateSubResponseDto(
                existingSubscription.getId(), testPhoneNumber, testChannel, existingStatus, newStatus))
                .thenReturn(validateResponse);

        // Given 3. 기존 구독이 존재할 경우 save()가 아닌 update() 실행
        when(subscriptionRepository.save(any(Subscription.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // 6. 최종 응답 변환 스텁
        SubscriptionResponseDto expectedResponse = new SubscriptionResponseDto(
                testChannel.getId(),
                testChannel.getName(),
                newStatus
        );
        when(subscriptionConverter.toSubscriptionResponseDto(any(Subscription.class), eq(testChannel)))
                .thenReturn(expectedResponse);


        // When: 구독 메서드 호출
        SubscriptionResponseDto response = premiumSubscriptionService.subscribe(
                new SubscriptionCreateRequestDto(testPhoneNumber, testChannel.getId(), newStatus)
        );

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getChannelId()).isEqualTo(testChannel.getId());
        assertThat(response.getChannelName()).isEqualTo(testChannel.getName());
        assertThat(response.getStatus()).isEqualTo(newStatus);

        ArgumentCaptor<Subscription> subscriptionCaptor = ArgumentCaptor.forClass(Subscription.class);
        verify(subscriptionRepository, times(1)).save(subscriptionCaptor.capture());

        Subscription savedSubscription = subscriptionCaptor.getValue();

        // 기존 구독이 업데이트되었는지 확인
        assertThat(savedSubscription.getId()).isEqualTo(existingSubscription.getId());
        assertThat(savedSubscription.getStatus()).isEqualTo(newStatus);
    }

    @Test
    void 중복_구독_예외_발생() {
        // Given: 중복 구독 요청 DTO 생성
        SubscriptionCreateRequestDto testRequestDto = new SubscriptionCreateRequestDto(testPhoneNumber, testChannel.getId(), SubscriptionStatus.SUBSCRIBE);

        // Given 1. 채널 DB 조회 스텁
        when(channelRepository.findById(testRequestDto.getChannelId()))
                .thenReturn(Optional.of(testChannel));

        // Given 2. 구독 여부 확인 스텁 (validateSubscribe 결과)
        ValidateSubscriptionResponseDto validateResponse =
                new ValidateSubscriptionResponseDto(null, testPhoneNumber, null, SubscriptionStatus.SUBSCRIBE, testChannel);
        when(subscriptionConverter.toValidateSubResponseDto(null, testPhoneNumber, testChannel, SubscriptionStatus.UNSUBSCRIBE, SubscriptionStatus.SUBSCRIBE))
                .thenReturn(validateResponse);

        // Given 3. 구독 중복 저장 시 `DataIntegrityViolationException` 발생하도록 설정
        when(subscriptionRepository.save(any(Subscription.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate entry"));

        // When & Then: 예외 발생 검증
        assertThatThrownBy(() -> basicSubscriptionService.subscribe(testRequestDto))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Duplicate entry");

        // Verify: 중복 저장 시 예외 발생 후, 추가 작업이 실행되지 않음
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
        verify(subscriptionHistoryRepository, times(0)).save(any(SubscriptionHistory.class));
    }

    @Test
    void 구독_해지_성공() {
        // Given: Request DTO 생성
        SubscriptionDeleteRequestDto testRequestDto = new SubscriptionDeleteRequestDto(testPhoneNumber, testChannel.getId(), SubscriptionStatus.UNSUBSCRIBE);
        SubscriptionDto subscriptionDto = new SubscriptionDto(1L, testPhoneNumber, SubscriptionStatus.SUBSCRIBE, true);
        // 1. 채널 DB 조회
        when(channelRepository.findById(testRequestDto.getChannelId()))
                .thenReturn(Optional.of(testChannel));

        // 2. 구독 여부 확인 (validateSubscribe 결과)
        ValidateUnSubscriptionResponseDto validateResponse =
                new ValidateUnSubscriptionResponseDto(testRequestDto.getChannelId(), testPhoneNumber, subscriptionDto.getStatus(), SubscriptionStatus.SUBSCRIBE, testChannel);
        when(subscriptionConverter.toValidateUnSubResponseDto(testRequestDto.getChannelId(), testPhoneNumber, testChannel, subscriptionDto.getStatus(), SubscriptionStatus.UNSUBSCRIBE))
                .thenReturn(validateResponse);

        // 5. 외부 API 호출 (강제 1 주입)
        when(csrngExternalService.fetchRandomData())
                .thenReturn(List.of(new CsrngResponseDto("success", 0, 1, 1)));

        // Repository의 save() 메서드는 호출 시 전달된 인자를 그대로 반환
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(subscriptionHistoryRepository.save(any(SubscriptionHistory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 6. 최종 응답 변환 스텁
        SubscriptionResponseDto expectedResponse = new SubscriptionResponseDto(
                testChannel.getId(),
                testChannel.getName(),
                SubscriptionStatus.UNSUBSCRIBE
        );
        when(subscriptionConverter.toSubscriptionResponseDto(any(Subscription.class), eq(testChannel)))
                .thenReturn(expectedResponse);

        // When: 구독 메서드 호출
        SubscriptionResponseDto response = basicSubscriptionService.unSubscribe(testRequestDto, subscriptionDto);

        // Then: 응답 검증
        assertThat(response).isNotNull();
        assertThat(response.getChannelId()).isEqualTo(testRequestDto.getChannelId());
        assertThat(response.getChannelName()).isEqualTo(testChannel.getName());
        assertThat(response.getStatus()).isEqualTo(SubscriptionStatus.UNSUBSCRIBE);

        // 추가: 각 저장 메서드의 호출 여부 검증
        verify(channelRepository, times(1)).findById(testRequestDto.getChannelId());
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
        verify(subscriptionHistoryRepository, times(1)).save(any(SubscriptionHistory.class));
        verify(csrngExternalService, times(1)).fetchRandomData();
        verify(subscriptionConverter, times(1)).toSubscriptionResponseDto(any(Subscription.class), eq(testChannel));
    }

}