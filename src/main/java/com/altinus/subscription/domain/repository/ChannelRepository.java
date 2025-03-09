package com.altinus.subscription.domain.repository;

import com.altinus.subscription.domain.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
}
