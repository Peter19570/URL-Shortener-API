package com.peter.shortener.features.auth.dto.response;

public record TokenResponse(
        String access,
        String refresh,
        long expiresAt
){}
