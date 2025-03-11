package com.artinus.subscription.application.service.impl;

import com.artinus.subscription.application.converter.SubscriptionConverter;
import com.artinus.subscription.application.dto.response.SubscriptionDto;
import com.artinus.subscription.application.dto.response.ValidateSubscriptionResponseDto;
import com.artinus.subscription.application.dto.response.ValidateUnSubscriptionResponseDto;
import com.artinus.subscription.domain.model.Channel;
import com.artinus.subscription.domain.model.Subscription;
import com.artinus.subscription.domain.model.enums.SubscriptionStatus;
import com.artinus.subscription.domain.repository.ChannelRepository;
import com.artinus.subscription.domain.repository.SubscriptionDSLRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PremiumSubscriptionServiceTest {
    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private SubscriptionConverter subscriptionConverter;

    @Mock
    private SubscriptionDSLRepository subscriptionDSLRepository;

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
    void 프리미엄_구독_신규() {
        // Given
        SubscriptionStatus status = SubscriptionStatus.PREMIUM_SUBSCRIBE;

        // Given 1. 기존 구독 없음
        when(subscriptionDSLRepository.findByPhoneNumberAndChannelIdDSL(testPhoneNumber, testChannel.getId()))
                .thenReturn(Optional.empty());

        // Given 2. 신규 구독 변환
        ValidateSubscriptionResponseDto expectedResponse =
                new ValidateSubscriptionResponseDto(null, testPhoneNumber, SubscriptionStatus.UNSUBSCRIBE, status, testChannel);

        when(subscriptionConverter.toValidateSubResponseDto(null, testPhoneNumber, testChannel, SubscriptionStatus.UNSUBSCRIBE, status))
                .thenReturn(expectedResponse);

        // When
        ValidateSubscriptionResponseDto response = premiumSubscriptionService.validateSubscribe(testPhoneNumber, testChannel, status);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNull();
        assertThat(response.getPhoneNumber()).isEqualTo(testPhoneNumber);
        assertThat(response.getNewStatus()).isEqualTo(status);
    }

    @Test
    void 프리미엄_구독_변경() {
        // Given
        SubscriptionStatus newStatus = SubscriptionStatus.PREMIUM_SUBSCRIBE;
        SubscriptionStatus existingStatus = SubscriptionStatus.SUBSCRIBE;
        Subscription existingSubscription = new Subscription(1L, testPhoneNumber, existingStatus, testChannel);

        // Given 1. 기존 구독이 존재하는 경우
        when(subscriptionDSLRepository.findByPhoneNumberAndChannelIdDSL(testPhoneNumber, testChannel.getId()))
                .thenReturn(Optional.of(existingSubscription));

        // Given 2. 기존 구독 반환
        ValidateSubscriptionResponseDto expectedResponse =
                new ValidateSubscriptionResponseDto(existingSubscription.getId(), testPhoneNumber, existingStatus, newStatus, testChannel);

        when(subscriptionConverter.toValidateSubResponseDto(
                existingSubscription.getId(), testPhoneNumber, testChannel, existingStatus, newStatus))
                .thenReturn(expectedResponse);

        // When
        ValidateSubscriptionResponseDto response = premiumSubscriptionService.validateSubscribe(testPhoneNumber, testChannel, newStatus);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getPhoneNumber()).isEqualTo(testPhoneNumber);
        assertThat(response.getOldStatus()).isEqualTo(existingStatus);
        assertThat(response.getNewStatus()).isEqualTo(newStatus);
    }

    @Test
    void 프리미엄_구독_해지() {
        // Given
        SubscriptionStatus status = SubscriptionStatus.SUBSCRIBE;
        SubscriptionDto subscriptionDto = new SubscriptionDto(1L, testPhoneNumber, SubscriptionStatus.PREMIUM_SUBSCRIBE, true);

        // Given 2. 신규 구독 변환
        ValidateUnSubscriptionResponseDto expectedResponse =
                new ValidateUnSubscriptionResponseDto(subscriptionDto.getId(), testPhoneNumber, subscriptionDto.getStatus(), status, testChannel);

        when(subscriptionConverter.toValidateUnSubResponseDto(subscriptionDto.getId(), testPhoneNumber, testChannel, subscriptionDto.getStatus(), status))
                .thenReturn(expectedResponse);

        // When
        ValidateUnSubscriptionResponseDto response = premiumSubscriptionService.validateUnSubscribe(testPhoneNumber, testChannel, status, subscriptionDto);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getPhoneNumber()).isEqualTo(testPhoneNumber);
        assertThat(response.getNewStatus()).isEqualTo(status);
    }
}