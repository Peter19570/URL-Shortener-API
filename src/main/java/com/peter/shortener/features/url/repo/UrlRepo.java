package com.peter.shortener.features.url.repo;

import com.peter.shortener.features.url.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UrlRepo extends JpaRepository<Url, UUID>, JpaSpecificationExecutor<Url> {

    Optional<Url> findByIdAndOwnerId(UUID urlId, UUID ownerId);

    Optional<Url> findByCode(Long code);

    @Modifying
    @Query("update Url u set u.clickCount = u.clickCount + 1, u.lastAccessedAt = :now where u.id = :id")
    void recordClick(@Param("id") UUID id, @Param("now") Instant now);
}
