package com.artinus.subscription.application.service;

import com.artinus.common.exception.ApiErrorException;
import com.artinus.common.response.enums.ResultCode;
import com.artinus.subscription.application.converter.ChannelConverter;
import com.artinus.subscription.application.converter.SubscriptionConverter;
import com.artinus.subscription.application.dto.request.SubscriptionCreateRequestDto;
import com.artinus.subscription.application.dto.request.SubscriptionDeleteRequestDto;
import com.artinus.subscription.application.dto.response.ChannelListResponseDto;
import com.artinus.subscription.application.dto.response.SubscriptionResponseDto;
import com.artinus.subscription.application.dto.response.ValidateSubscriptionResponseDto;
import com.artinus.subscription.domain.model.Channel;
import com.artinus.subscription.domain.model.Subscription;
import com.artinus.subscription.domain.model.SubscriptionHistory;
import com.artinus.subscription.domain.repository.ChannelRepository;
import com.artinus.subscription.domain.repository.SubscriptionDSLRepository;
import com.artinus.subscription.domain.repository.SubscriptionHistoryRepository;
import com.artinus.subscription.domain.repository.SubscriptionRepository;
import com.artinus.subscription.infrastructure.external.dto.CsrngResponseDto;
import com.artinus.subscription.infrastructure.external.service.CsrngExternalService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public abstract class AbstractSubscriptionService implements SubscriptionService {

    protected final ChannelRepository channelRepository;
    protected final SubscriptionRepository subscriptionRepository;
    protected final SubscriptionHistoryRepository subscriptionHistoryRepository;
    protected final SubscriptionDSLRepository subscriptionDSLRepository;
    protected final ChannelConverter channelConverter;
    protected final SubscriptionConverter subscriptionConverter;
    protected final CsrngExternalService csrngExternalService;

    /**
     * 구독 가능 채널: 홈페이지, 모바일앱, 네이버, SKT, KT, LGU+
     * 해지 가능 채널: 홈페이지, 모바일앱, 콜센터, 채팅상담, 이메일
     */
    @PostConstruct
    public void init() {
        // 구독, 해지 가능
        saveChannelIfNot("홈페이지", true);
        saveChannelIfNot("모바일앱", true);

        // 구독만 가능
        saveChannelIfNot("네이버", false);
        saveChannelIfNot("SKT", false);
        saveChannelIfNot("KT", false);
        saveChannelIfNot("LGU+", false);

        // 해지만 가능
        saveChannelIfNot("콜센터", true);
        saveChannelIfNot("채팅상담", true);
        saveChannelIfNot("이메일", true);
    }

    /**
     * 채널의 목록을 보여주는 API
     * 공통 메서드이기에 해당 클래스에서 구현
     *
     * @return List<ChannelListResponseDto>
     */
    @Override
    public List<ChannelListResponseDto> findAllChannels() {
        List<Channel> channels = channelRepository.findAllByOrderByCreatedDate();
        return channelConverter.toResponseDtoList(channels);
    }

    /**
     * csrng.net 외부 API 트랜잭션 요청
     * 1 -> 트랜잭션 수행
     * 0 -> 예외를 던져 롤백 시도
     *
     * @return
     * @throws ApiErrorException
     */
    @Override
    public Boolean callAltinusSubscribe() {
        List<CsrngResponseDto> temp = csrngExternalService.fetchRandomData();
        int result = temp.get(0).getRandom();
        log.info("External Response => {}", result);
        if(result == 0) {
            throw new ApiErrorException(ResultCode.CSRNG_ROLL_BACK);
        }
        return true;
    }

    /**
     * 구독하기 API
     *
     * @param subscriptionCreateRequestDto
     * @return SubscriptionResponseDto
     */
    @Override
    @Transactional
    public SubscriptionResponseDto subscribe(SubscriptionCreateRequestDto subscriptionCreateRequestDto) {
        // 1. 채널 DB 데이터 조회
        log.info("[구독 시작]");
        log.info("[구독] 1. 채널 DB 데이터 조회 {} => {}", subscriptionCreateRequestDto.getPhoneNumber(), subscriptionCreateRequestDto.getChannelId());
        Channel channelEntity = channelRepository.findById(subscriptionCreateRequestDto.getChannelId())
                .orElseThrow(() -> new ApiErrorException(ResultCode.NOT_FOUND));

        // 2. 구독 여부 확인
        log.info("[구독] 2. 구독 여부 확인");
        ValidateSubscriptionResponseDto validateSubscriptionResponseDto = validateSubscribe(
                subscriptionCreateRequestDto.getPhoneNumber(),
                channelEntity,
                subscriptionCreateRequestDto.getStatus()
                );

        // 3. 구독
        Subscription newSubscription = new Subscription(
                validateSubscriptionResponseDto.getPhoneNumber(),
                validateSubscriptionResponseDto.getChannel(),
                validateSubscriptionResponseDto.getNewStatus()
        );
        subscriptionRepository.save(newSubscription);
        log.info("[구독] 3. 구독 {} => {}", validateSubscriptionResponseDto.getPhoneNumber(), validateSubscriptionResponseDto.getChannel().getName());

        // 4. 구독 내역 저장
        SubscriptionHistory newSubscriptionHistory = new SubscriptionHistory(
                validateSubscriptionResponseDto.getPhoneNumber(),
                validateSubscriptionResponseDto.getOldStatus(),
                validateSubscriptionResponseDto.getNewStatus(),
                validateSubscriptionResponseDto.getChannel().getName()
        );
        subscriptionHistoryRepository.save(newSubscriptionHistory);
        log.info("[구독] 4. 구독 이력 {} => {}", validateSubscriptionResponseDto.getOldStatus(), validateSubscriptionResponseDto.getNewStatus());

        // 5. 외부 API 호출
        callAltinusSubscribe();
        log.info("[구독 완료]");
        return subscriptionConverter.toSubscriptionResponseDto(newSubscription, channelEntity);
    }

    /**
     * 구독 취소하기 API
     *
     * @param subscriptionDeleteRequestDto
     */
    @Override
    @Transactional
    public void unSubscription(SubscriptionDeleteRequestDto subscriptionDeleteRequestDto) {
        // 외부 API 호출
        callAltinusSubscribe();
    }


    private void saveChannelIfNot(String name, Boolean type) {
        channelRepository.findByName(name)
                .orElseGet(() -> channelRepository.save(new Channel(name, type)));
    }
}
