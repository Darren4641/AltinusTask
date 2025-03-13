package com.artinus.subscription.domain.channel;

import com.artinus.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "channels")
public class Channel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Boolean canSubscribe;

    @Column(nullable = false)
    private Boolean canUnSubscribe;

    public Channel(String name, Boolean canSubscribe, Boolean canUnSubscribe) {
        this.name = name;
        this.canSubscribe = canSubscribe;
        this.canUnSubscribe = canUnSubscribe;
    }

}
