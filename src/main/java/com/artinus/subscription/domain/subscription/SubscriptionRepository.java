package com.artinus.subscription.domain.subscription;

import com.artinus.subscription.domain.subscription.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}
