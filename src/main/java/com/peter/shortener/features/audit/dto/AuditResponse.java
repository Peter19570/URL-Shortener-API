package com.peter.shortener.features.audit.dto;

import com.peter.shortener.features.audit.enums.AuditAction;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record AuditResponse(
        UUID id,
        UUID userId,
        String email,
        String fullName,
        AuditAction action,
        String description,
        String ipAddress,
        String userAgent,
        Map<String, Object> metadata,
        Instant createdAt
) {
}
