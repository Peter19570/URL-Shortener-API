package com.peter.shortener.features.url.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record UrlRequest(

        @NotBlank
        @Size(max = 2048)
        @URL(regexp = "^(https?)://.*")
        String originalUrl,

        @Size(max = 100)
        String title,

        @Min(1)
        @Max(365)
        Integer expiresInDays

) {
}
