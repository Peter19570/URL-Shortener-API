package com.peter.shortener.features.url.model;

import com.fayupable.snowflake.jpa.SnowflakeGeneratedId;
import com.peter.shortener.features.shared.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "urls")
public class Url extends BaseEntity {

    @Column(nullable = false, unique = true)
    private Long code;

    @Column( nullable = false, length = 2048)
    private String originalUrl;

    private String title;
    private long clickCount = 0;
    private Instant expiresAt;
    private Instant lastAccessedAt;
    private UUID ownerId;
}
