package com.altinus.subscription.domain.repository;

import com.altinus.subscription.domain.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}
