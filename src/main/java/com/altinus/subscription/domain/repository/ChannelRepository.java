package com.altinus.subscription.domain.repository;

import com.altinus.subscription.domain.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    Optional<Channel> findByName(String name);

    List<Channel> findAllByOrderByCreatedDate();

}
