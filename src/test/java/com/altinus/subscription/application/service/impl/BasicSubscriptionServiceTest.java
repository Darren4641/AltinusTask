package com.altinus.subscription.application.service.impl;

import com.altinus.common.exception.ApiErrorException;
import com.altinus.common.response.enums.ResultCode;
import com.altinus.subscription.application.converter.ChannelConverter;
import com.altinus.subscription.application.converter.SubscriptionConverter;
import com.altinus.subscription.application.dto.response.ValidateSubscriptionResponseDto;
import com.altinus.subscription.domain.model.Channel;
import com.altinus.subscription.domain.model.Subscription;
import com.altinus.subscription.domain.model.enums.SubscriptionStatus;
import com.altinus.subscription.domain.repository.ChannelRepository;
import com.altinus.subscription.domain.repository.SubscriptionDSLRepository;
import com.altinus.subscription.domain.repository.SubscriptionHistoryRepository;
import com.altinus.subscription.domain.repository.SubscriptionRepository;
import com.altinus.subscription.infrastructure.external.service.CsrngExternalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasicSubscriptionServiceTest {
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private SubscriptionHistoryRepository subscriptionHistoryRepository;
    @Mock
    private SubscriptionDSLRepository subscriptionDSLRepository;
    @Mock
    private SubscriptionConverter subscriptionConverter;
    @Mock
    private CsrngExternalService csrngExternalService;

    @InjectMocks
    private BasicSubscriptionService basicSubscriptionService;

    private Channel testChannel;
    private String testPhoneNumber;

    @BeforeEach
    void setUp() {
        testChannel = new Channel(1L, "Test Channel", true);
        channelRepository.save(testChannel);
        testPhoneNumber = "01012345678";
    }

    @Test
    void 일반_구독_가능한_경우() {
        // Given
        SubscriptionStatus status = SubscriptionStatus.SUBSCRIBE;
        when(subscriptionDSLRepository.findByPhoneNumberAndChannelIdAndStatusDSL(
                testPhoneNumber, testChannel.getId(), status)
        ).thenReturn(Optional.empty());

        ValidateSubscriptionResponseDto expectedResponse =
                new ValidateSubscriptionResponseDto(testPhoneNumber, null, status, testChannel);

        when(subscriptionConverter.toValidateResponseDto(testPhoneNumber, testChannel, null, status))
                .thenReturn(expectedResponse);

        // When
        ValidateSubscriptionResponseDto response = basicSubscriptionService.validateSubscribe(testPhoneNumber, testChannel, status);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getPhoneNumber()).isEqualTo(testPhoneNumber);
        assertThat(response.getNewStatus()).isEqualTo(status);
    }

    @Test
    void 이미_구독중이면_예외발생() {
        // Given
        SubscriptionStatus status = SubscriptionStatus.SUBSCRIBE;
        Subscription existingSubscription = new Subscription(testPhoneNumber, testChannel, status);
        when(subscriptionDSLRepository.findByPhoneNumberAndChannelIdAndStatusDSL(
                testPhoneNumber, testChannel.getId(), status)
        ).thenReturn(Optional.of(existingSubscription));

        assertThatThrownBy(() -> basicSubscriptionService.validateSubscribe(testPhoneNumber, testChannel, status))
                .isInstanceOf(ApiErrorException.class)
                .hasMessageContaining(ResultCode.ALREADY_REQUEST.getMessage());
    }
}