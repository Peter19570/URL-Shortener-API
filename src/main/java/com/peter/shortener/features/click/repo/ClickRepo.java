package com.peter.shortener.features.click.repo;

import com.peter.shortener.features.click.model.Click;
import com.peter.shortener.features.url.dto.response.UrlStats;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface ClickRepo extends JpaRepository<Click, UUID> {
    void deleteByUrlId(UUID urlId);

    @Query("select count(distinct c.ipHash) from Click c where c.url.id = :urlId")
    long countUniqueVisitors(@Param("urlId") UUID urlId);

    @Query("""
        select new com.peter.shortener.features.url.dto.response.UrlStats$DailyClicks(
            cast(c.clickedAt as LocalDate), count(c))
        from Click c
        where c.url.id = :urlId and c.clickedAt >= :since
        group by cast(c.clickedAt as LocalDate)
        order by cast(c.clickedAt as LocalDate)
        """)
    List<UrlStats.DailyClicks> clicksPerDay(@Param("urlId") UUID urlId, @Param("since") Instant since);

    @Query("""
        select new com.peter.shortener.features.url.dto.response.UrlStats$ReferrerCount(
            coalesce(c.referrer, 'direct'), count(c))
        from Click c
        where c.url.id = :urlId
        group by coalesce(c.referrer, 'direct')
        order by count(c) desc
        """)
    List<UrlStats.ReferrerCount> topReferrers(@Param("urlId") UUID urlId, Pageable pageable);
}
