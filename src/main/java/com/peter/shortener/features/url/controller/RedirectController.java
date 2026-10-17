package com.peter.shortener.features.url.controller;

import com.peter.shortener.features.shared.dto.ApiResponse;
import com.peter.shortener.features.url.dto.response.UrlResponse;
import com.peter.shortener.features.url.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "URL Redirect",
        description = "Endpoints for resolving shortened URLs and redirecting users to their original destinations."
)
public class RedirectController {

    private final UrlService urlService;

    @GetMapping("/{code:[0-9A-Za-z]{1,12}}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        String target = urlService.resolveAndTrack(code); // returns the original URL string
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, URI.create(target).toString())
                .build();
    }
}
