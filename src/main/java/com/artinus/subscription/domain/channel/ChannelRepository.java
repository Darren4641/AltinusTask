package com.artinus.subscription.domain.channel;

import com.artinus.subscription.domain.channel.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    Optional<Channel> findByName(String name);

    List<Channel> findAllByOrderByCreatedDate();

}
