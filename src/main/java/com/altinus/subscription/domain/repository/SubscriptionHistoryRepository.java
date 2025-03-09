package com.altinus.subscription.domain.repository;

import com.altinus.subscription.domain.model.SubscriptionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionHistoryRepository extends JpaRepository<SubscriptionHistory, Long> {
}
