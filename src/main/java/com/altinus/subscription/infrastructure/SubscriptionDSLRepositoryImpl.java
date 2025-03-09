package com.altinus.subscription.infrastructure;

import com.altinus.subscription.domain.repository.SubscriptionDSLRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class SubscriptionDSLRepositoryImpl implements SubscriptionDSLRepository {
    private final JPAQueryFactory queryFactory;

    public SubscriptionDSLRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }



}
