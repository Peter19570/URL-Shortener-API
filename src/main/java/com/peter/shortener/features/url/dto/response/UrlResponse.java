package com.peter.shortener.features.url.dto.response;

import java.util.UUID;

public record UrlResponse(
        UUID id,
        String code,
        String originalUrl,
        String title
) {
}
