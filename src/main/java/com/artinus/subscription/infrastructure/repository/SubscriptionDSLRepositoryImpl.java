package com.artinus.subscription.infrastructure.repository;

import com.artinus.subscription.application.dto.response.SubscriptionDetailResponseDto;
import com.artinus.subscription.application.dto.response.SubscriptionDto;
import com.artinus.subscription.application.dto.response.SubscriptionHistoryDto;
import com.artinus.subscription.domain.subscription.Subscription;
import com.artinus.subscription.domain.common.SubscriptionStatus;
import com.artinus.subscription.domain.common.SubscriptionDSLRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.artinus.subscription.domain.channel.QChannel.channel;
import static com.artinus.subscription.domain.history.QSubscriptionHistory.subscriptionHistory;
import static com.artinus.subscription.domain.subscription.QSubscription.subscription;


@Repository
public class SubscriptionDSLRepositoryImpl implements SubscriptionDSLRepository {
    private final JPAQueryFactory queryFactory;

    public SubscriptionDSLRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Optional<Subscription> findByPhoneNumberAndChannelIdAndStatusDSL(String phoneNumber, Long channelId, SubscriptionStatus status) {
        return Optional.ofNullable(queryFactory
                .select(subscription)
                .from(subscription)
                .join(subscription.channel, channel)
                .where(subscription.phoneNumber.eq(phoneNumber)
                        .and(channel.id.eq(channelId))
                        .and(subscription.status.eq(status)))
                .fetchOne());
    }

    public Optional<Subscription> findByPhoneNumberAndChannelIdDSL(String phoneNumber, Long channelId) {
        return Optional.ofNullable(queryFactory
                .select(subscription)
                .from(subscription)
                .join(subscription.channel, channel)
                .where(subscription.phoneNumber.eq(phoneNumber)
                        .and(channel.id.eq(channelId)))
                .fetchOne());
    }

    @Override
    public Optional<SubscriptionDto> findSubscriptionDSL(String phoneNumber, Long channelId) {
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(
                        SubscriptionDto.class,
                        subscription.id,
                        subscription.phoneNumber,
                        subscription.status,
                        channel.canUnSubscribe
                ))
                .from(subscription)
                .join(subscription.channel, channel)
                .where(subscription.phoneNumber.eq(phoneNumber)
                        .and(channel.id.eq(channelId)))
                .fetchOne());
    }

    public Map<SubscriptionStatus, List<SubscriptionDetailResponseDto>> findSubscriptionsDetailByPhoneNumberDSL(String phoneNumber) {
        List<Tuple> result = queryFactory
                .select(
                        subscription.id,
                        channel.id,
                        channel.name,
                        subscription.status,
                        channel.canUnSubscribe
                )
                .from(subscription)
                .join(subscription.channel, channel)
                .where(subscription.phoneNumber.eq(phoneNumber))
                .fetch();

        return result.stream()
                .map(it -> new SubscriptionDetailResponseDto(
                        it.get(subscription.id),
                        it.get(channel.id),
                        it.get(channel.name),
                        it.get(subscription.status),
                        it.get(channel.canUnSubscribe)
                ))
                .collect(Collectors.groupingBy(SubscriptionDetailResponseDto::getStatus));
    }

    @Override
    public Page<SubscriptionHistoryDto> findSubscriptionHistories(String phoneNumber, Pageable pageable) {
        List<SubscriptionHistoryDto> results = queryFactory
                .select(Projections.constructor(
                        SubscriptionHistoryDto.class,
                        subscriptionHistory.id,
                        subscriptionHistory.channelName,
                        subscriptionHistory.oldStatus,
                        subscriptionHistory.newStatus,
                        subscriptionHistory.updatedDate
                ))
                .from(subscriptionHistory)
                .where(subscriptionHistory.phoneNumber.eq(phoneNumber))
                .orderBy(subscriptionHistory.updatedDate.desc())
                .offset(pageable.getOffset())  // 페이징 적용
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(subscriptionHistory.id.count())
                .from(subscriptionHistory)
                .fetchOne();

        return new PageImpl<>(results, pageable, total != null ? total : 0L);
    }

}
