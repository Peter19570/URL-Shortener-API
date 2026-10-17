package com.peter.shortener.features.url.specification;

import com.peter.shortener.features.url.filters.UrlFilter;
import com.peter.shortener.features.url.model.Url;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UrlSpecification {

    public static Specification<Url> withFilter(UrlFilter filter) {
        Specification<Url> spec = Specification.unrestricted();

        if (filter.getCode() != null) {
            spec = spec.and((root, _, cb) ->
                    cb.equal(root.get("code"), filter.getCode()));
        }

        if (filter.getTitle() != null){
            spec = spec.and((root, _, cb) ->
                    cb.like(cb.lower(root.get("title")), "%" + filter.getTitle().toLowerCase() + "%"));
        }

        if (filter.getOriginalUrl() != null) {
            spec = spec.and((root, _, cb) ->
                    cb.equal(root.get("originalUrl"), filter.getOriginalUrl()));
        }

        if (filter.getCreatedBefore() != null) {
            spec = spec.and((root, _, cb) ->
                    cb.lessThan(root.get("createdAt"), filter.getCreatedBefore()));
        }

        if (filter.getCreatedAfter() != null) {
            spec = spec.and((root, _, cb) ->
                    cb.greaterThan(root.get("createdAt"), filter.getCreatedAfter()));
        }

        if (filter.getExpiresBefore() != null) {
            spec = spec.and((root, _, cb) ->
                    cb.lessThan(root.get("expiresAt"), filter.getExpiresBefore()));
        }

        if (filter.getExpiresAfter() != null) {
            spec = spec.and((root, _, cb) ->
                    cb.greaterThan(root.get("expiresAt"), filter.getExpiresAfter()));
        }

        if (filter.isNeverExpires()) {
            spec = spec.and((root, _, cb) ->
                    cb.isNull(root.get("expiresAt")));
        }

        return spec;
    }

    public static Specification<Url> ownedBy(UUID ownerId) {
        return (root, _, cb) -> 
                cb.equal(root.get("ownerId"), ownerId);
    }
}
