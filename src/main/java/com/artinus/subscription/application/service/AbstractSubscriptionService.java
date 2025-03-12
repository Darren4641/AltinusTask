package com.artinus.subscription.application.service;

import com.artinus.common.exception.ApiErrorException;
import com.artinus.common.response.enums.ResultCode;
import com.artinus.subscription.application.converter.ChannelConverter;
import com.artinus.subscription.application.converter.SubscriptionConverter;
import com.artinus.subscription.application.converter.SubscriptionHistoryConverter;
import com.artinus.subscription.application.dto.request.SubscriptionCreateRequestDto;
import com.artinus.subscription.application.dto.request.SubscriptionDeleteRequestDto;
import com.artinus.subscription.application.dto.response.*;
import com.artinus.subscription.domain.model.Channel;
import com.artinus.subscription.domain.model.Subscription;
import com.artinus.subscription.domain.model.SubscriptionHistory;
import com.artinus.subscription.domain.model.enums.SubscriptionStatus;
import com.artinus.subscription.domain.repository.ChannelRepository;
import com.artinus.subscription.domain.repository.SubscriptionDSLRepository;
import com.artinus.subscription.domain.repository.SubscriptionHistoryRepository;
import com.artinus.subscription.domain.repository.SubscriptionRepository;
import com.artinus.subscription.infrastructure.external.dto.CsrngResponseDto;
import com.artinus.subscription.infrastructure.external.service.CsrngExternalService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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
    protected final SubscriptionHistoryConverter subscriptionHistoryConverter;
    protected final CsrngExternalService csrngExternalService;

    /**
     * 구독 가능 채널: 홈페이지, 모바일앱, 네이버, SKT, KT, LGU+
     * 해지 가능 채널: 홈페이지, 모바일앱, 콜센터, 채팅상담, 이메일
     */
    @PostConstruct
    public void init() {
        // 구독, 해지 가능
        saveChannelIfNot("홈페이지", true, true);
        saveChannelIfNot("모바일앱", true, true);

        // 구독만 가능
        saveChannelIfNot("네이버", true, false);
        saveChannelIfNot("SKT", true, false);
        saveChannelIfNot("KT", true, false);
        saveChannelIfNot("LGU+", true, false);

        // 해지만 가능
        saveChannelIfNot("콜센터", false, true);
        saveChannelIfNot("채팅상담", false, true);
        saveChannelIfNot("이메일", false, true);
    }

    /**
     * 채널의 목록을 보여주는 로직
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
     * 구독하기 로직
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

        if(!channelEntity.getCanSubscribe()) {
            throw new ApiErrorException(ResultCode.DISABLED_SUBSCRIBE);
        }

        // 2. 구독 여부 확인
        // 각각 구현체에서 구현
        log.info("[구독] 2. 구독 여부 확인");
        ValidateSubscriptionResponseDto validateSubscriptionResponseDto = validateSubscribe(
                subscriptionCreateRequestDto.getPhoneNumber(),
                channelEntity,
                subscriptionCreateRequestDto.getStatus()
                );

        // 3. 구독 - validateSubscriptionResponseDto.getId() 로 JPA의 더티체킹
        Subscription subscriptionEntity = new Subscription(
                validateSubscriptionResponseDto.getId(),
                validateSubscriptionResponseDto.getPhoneNumber(),
                validateSubscriptionResponseDto.getNewStatus(),
                validateSubscriptionResponseDto.getChannel()
        );
        subscriptionRepository.save(subscriptionEntity);
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
        return subscriptionConverter.toSubscriptionResponseDto(subscriptionEntity, channelEntity);
    }

    /**
     * 구독 취소하기 로직
     *
     * @param subscriptionDeleteRequestDto
     * @return
     */
    @Override
    @Transactional
    public SubscriptionResponseDto unSubscribe(SubscriptionDeleteRequestDto subscriptionDeleteRequestDto, SubscriptionDto subscriptionDto) {
        log.info("[구독 해지 시작]");
        log.info("[구독 해지] 1. 채널 DB 데이터 조회 {} => {}", subscriptionDeleteRequestDto.getPhoneNumber(), subscriptionDeleteRequestDto.getChannelId());
        Channel channelEntity = channelRepository.findById(subscriptionDeleteRequestDto.getChannelId())
                .orElseThrow(() -> new ApiErrorException(ResultCode.NOT_FOUND));

        if(!channelEntity.getCanUnSubscribe()) {
            throw new ApiErrorException(ResultCode.DISABLED_UNSUBSCRIBE);
        }

        /*
        * 만약 프리미엄 구독 -> 일반 구독 은 가능한다그러면 아래 코드로 예외처리 변경
        if(!channelEntity.getCanUnSubscribe() &&
          subscriptionDeleteRequestDto.getStatus() == SubscriptionStatus.UNSUBSCRIBE) {
            throw new ApiErrorException(ResultCode.DISABLED_UNSUBSCRIBE);
        }*/

        // 2. 구독 해지 여부 확인
        // 각각 구현체에서 구현
        log.info("[구독 해지] 2. 구독 해지 여부 확인");
        ValidateUnSubscriptionResponseDto validateUnSubscriptionResponseDto = validateUnSubscribe(
                subscriptionDeleteRequestDto.getPhoneNumber(),
                channelEntity,
                subscriptionDeleteRequestDto.getStatus(),
                subscriptionDto
        );

        Subscription subscriptionEntity = new Subscription(
                validateUnSubscriptionResponseDto.getId(),
                validateUnSubscriptionResponseDto.getPhoneNumber(),
                validateUnSubscriptionResponseDto.getNewStatus(),
                validateUnSubscriptionResponseDto.getChannel()
        );

        if(validateUnSubscriptionResponseDto.getNewStatus() == SubscriptionStatus.UNSUBSCRIBE) {
            subscriptionRepository.deleteById(subscriptionEntity.getId());
        } else {
            subscriptionRepository.save(subscriptionEntity);
        }
        log.info("[구독 해지] 3. 구독 {} => {}", validateUnSubscriptionResponseDto.getPhoneNumber(), validateUnSubscriptionResponseDto.getChannel().getName());

        // 4. 구독 내역 저장
        SubscriptionHistory newSubscriptionHistory = new SubscriptionHistory(
                validateUnSubscriptionResponseDto.getPhoneNumber(),
                validateUnSubscriptionResponseDto.getOldStatus(),
                validateUnSubscriptionResponseDto.getNewStatus(),
                validateUnSubscriptionResponseDto.getChannel().getName()
        );
        subscriptionHistoryRepository.save(newSubscriptionHistory);
        log.info("[구독] 4. 구독 이력 {} => {}", validateUnSubscriptionResponseDto.getOldStatus(), validateUnSubscriptionResponseDto.getNewStatus());

        // 외부 API 호출
        callAltinusSubscribe();
        log.info("[구독 해지 완료]");
        return subscriptionConverter.toSubscriptionResponseDto(subscriptionEntity, channelEntity);
    }

    /**
     * 구독 상태 가져오기 로직
     * @param phoneNumber
     * @return
     */
    @Override
    public SubscriptionDto getSubscription(String phoneNumber, Long channelId) {
        return subscriptionDSLRepository.findSubscriptionDSL(phoneNumber, channelId)
                .orElse(new SubscriptionDto(SubscriptionStatus.UNSUBSCRIBE));
    }

    @Override
    public Map<SubscriptionStatus, List<SubscriptionDetailResponseDto>> getMySubscriptions(String phoneNumber) {
        return subscriptionDSLRepository.findSubscriptionsDetailByPhoneNumberDSL(phoneNumber);
    }

    @Override
    public Page<SubscriptionHistoryDto> getMySubscriptionHistories(String phoneNumber, Pageable pageable) {
        return subscriptionDSLRepository.findSubscriptionHistories(phoneNumber, pageable);
    }


    private void saveChannelIfNot(String name, Boolean canSubscribe, Boolean canUnSubscribe) {
        channelRepository.findByName(name)
                .orElseGet(() -> channelRepository.save(new Channel(name, canSubscribe, canUnSubscribe)));
    }
}
