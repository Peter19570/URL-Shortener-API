package com.peter.shortener.features.click.model;

import com.peter.shortener.features.shared.model.BaseEntity;
import com.peter.shortener.features.url.model.Url;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "clicks")
public class Click extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "url_id")
    private Url url;

    private Instant clickedAt = Instant.now();

    private String ipHash;

    @Column(length = 512)
    private String userAgent;

    @Column(length = 2048)
    private String referrer;
}
