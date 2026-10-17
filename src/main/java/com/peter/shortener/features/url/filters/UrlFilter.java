package com.peter.shortener.features.url.filters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class UrlFilter {

    private Long code;
    private String title;
    private String originalUrl;
    private Instant createdBefore;
    private Instant createdAfter;
    private Instant expiresBefore;
    private Instant expiresAfter;
    private boolean neverExpires;

}
