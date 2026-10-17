package com.peter.shortener.features.url.dto.response;

import java.time.Instant;
import java.util.UUID;

public record UrlDetailedResponse(
        UUID id,
        String code,
        String title,
        String originalUrl,
        long clickCount,
        Instant createdAt,
        Instant expiresAt,
        Instant lastAccessedAt,
        UrlStats stats
) {
    public UrlDetailedResponse withStats(UrlStats stats) {
        return new UrlDetailedResponse(id, code, title, originalUrl, clickCount,
                createdAt, expiresAt, lastAccessedAt, stats);
    }
}
