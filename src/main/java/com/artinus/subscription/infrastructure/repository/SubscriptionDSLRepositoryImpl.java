package com.artinus.subscription.infrastructure.repository;

import com.artinus.subscription.domain.model.Subscription;
import com.artinus.subscription.domain.model.enums.SubscriptionStatus;
import com.artinus.subscription.domain.repository.SubscriptionDSLRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.artinus.subscription.domain.model.QChannel.channel;
import static com.artinus.subscription.domain.model.QSubscription.subscription;

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

}
