package com.peter.shortener.features.url.dto.response;

import java.util.List;

public record UrlStats(
        long uniqueVisitors,
        List<DailyClicks> clicksPerDay,
        List<ReferrerCount> topReferrers
) {
    public record DailyClicks(java.time.LocalDate date, long clicks) {}
    public record ReferrerCount(String referrer, long clicks) {}
}
