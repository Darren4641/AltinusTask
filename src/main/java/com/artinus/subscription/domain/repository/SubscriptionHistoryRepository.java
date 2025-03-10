package com.artinus.subscription.domain.repository;

import com.artinus.subscription.domain.model.SubscriptionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionHistoryRepository extends JpaRepository<SubscriptionHistory, Long> {
}
