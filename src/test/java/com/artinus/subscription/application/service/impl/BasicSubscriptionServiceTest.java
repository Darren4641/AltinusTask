package com.artinus.subscription.application.service.impl;

import com.artinus.subscription.application.converter.SubscriptionConverter;
import com.artinus.subscription.application.dto.response.ValidateSubscriptionResponseDto;
import com.artinus.subscription.domain.model.Channel;
import com.artinus.subscription.domain.model.enums.SubscriptionStatus;
import com.artinus.subscription.domain.repository.ChannelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasicSubscriptionServiceTest {
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private SubscriptionConverter subscriptionConverter;

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
    void 일반_구독() {
        // Given
        SubscriptionStatus status = SubscriptionStatus.SUBSCRIBE;

        ValidateSubscriptionResponseDto expectedResponse =
                new ValidateSubscriptionResponseDto(null, testPhoneNumber, null, status, testChannel);

        when(subscriptionConverter.toValidateResponseDto(null, testPhoneNumber, testChannel, null, status))
                .thenReturn(expectedResponse);

        // When
        ValidateSubscriptionResponseDto response = basicSubscriptionService.validateSubscribe(testPhoneNumber, testChannel, status);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getPhoneNumber()).isEqualTo(testPhoneNumber);
        assertThat(response.getNewStatus()).isEqualTo(status);
    }

}