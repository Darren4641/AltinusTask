package com.altinus.subscription.domain.model;

import com.altinus.subscription.domain.model.enums.ChannelType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "channels")
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChannelType type;

    public boolean canSubscribe() {
        return type == ChannelType.SUBSCRIBE_ONLY || type == ChannelType.BOTH;
    }

    public boolean canUnsubscribe() {
        return type == ChannelType.UNSUBSCRIBE_ONLY || type == ChannelType.BOTH;
    }

}
