package com.artinus.subscription.domain.model;

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
    private Boolean canUnSubscribe;

    public Channel(String name, Boolean canUnSubscribe) {
        this.name = name;
        this.canUnSubscribe = canUnSubscribe;
    }

}
