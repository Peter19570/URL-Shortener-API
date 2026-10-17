package com.peter.shortener.features.url.controller;

import com.peter.shortener.features.shared.dto.ApiResponse;
import com.peter.shortener.features.shared.dto.PageResponse;
import com.peter.shortener.features.shared.dto.UserPrincipal;
import com.peter.shortener.features.url.dto.request.UrlRequest;
import com.peter.shortener.features.url.dto.response.UrlDetailedResponse;
import com.peter.shortener.features.url.dto.response.UrlResponse;
import com.peter.shortener.features.url.filters.UrlFilter;
import com.peter.shortener.features.url.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

import static com.peter.shortener.features.shared.constants.PageConstants.DEFAULT_SORT_FIELD;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shortener")
@Tag(
        name = "URL Shortener",
        description = "Endpoints for creating, managing, and retrieving shortened URLs."
)
public class UrlController {

    private final UrlService urlService;

    @PostMapping
    @Operation(summary = "Generate Short URL")
    public ResponseEntity<ApiResponse<UrlResponse>> generateUrlCode(
            @RequestBody @Valid UrlRequest request,
            @AuthenticationPrincipal UserPrincipal principal){
        UrlResponse response = urlService.generateUrlCode(request, principal);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Url code generated", response));
    }

    @GetMapping
    @Operation(summary = "Get All Shortened URLs")
    public ResponseEntity<ApiResponse<PageResponse<UrlResponse>>> getAllUrls(
            @PageableDefault(page = 1, sort = DEFAULT_SORT_FIELD, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Long code,
            @RequestParam(required = false) String originalUrl,
            @RequestParam(required = false) Instant createdBefore,
            @RequestParam(required = false) Instant createdAfter,
            @RequestParam(required = false) Instant expiresBefore,
            @RequestParam(required = false) Instant expiresAfter,
            @RequestParam(required = false) boolean neverExpires,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        UrlFilter filter = UrlFilter.builder()
                .code(code)
                .originalUrl(originalUrl)
                .createdBefore(createdBefore)
                .createdAfter(createdAfter)
                .expiresBefore(expiresBefore)
                .expiresAfter(expiresAfter)
                .neverExpires(neverExpires)
                .build();

        PageResponse<UrlResponse> response = urlService.getAllUrls(pageable, filter, principal.id());
        return ResponseEntity.ok(ApiResponse.success("All user's urls", response));
    }

    @GetMapping("/{urlId}")
    @Operation(summary = "Get Shortened URL by ID")
    public ResponseEntity<ApiResponse<UrlDetailedResponse>> getUrlById(
            @PathVariable UUID urlId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        UrlDetailedResponse response = urlService.getUrlById(urlId, principal.id());
        return ResponseEntity.ok(ApiResponse.success("Url Details", response));
    }

    @PatchMapping("/{urlId}")
    @Operation(summary = "Update Shortened URL")
    public ResponseEntity<ApiResponse<UrlDetailedResponse>> updateUrl(
            @RequestBody @Valid UrlRequest request,
            @PathVariable UUID urlId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        UrlDetailedResponse response = urlService.updateUrl(request, urlId, principal.id());
        return ResponseEntity.ok(ApiResponse.success("Url updated", response));
    }

    @DeleteMapping("/{urlId}")
    @Operation(summary = "Delete Shortened URL")
    public ResponseEntity<Void> deleteUrl(
            @PathVariable UUID urlId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        urlService.deleteUrl(urlId, principal.id());
        return ResponseEntity.noContent().build();
    }
}
