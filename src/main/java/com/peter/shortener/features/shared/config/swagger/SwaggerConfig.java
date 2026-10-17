package com.peter.shortener.features.shared.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "URL Shortener",
                description = "A URL shortening service that converts long URLs into compact, shareable links, with fast redirects to the original destinations and unique link generation using Snowflake IDs.",
                contact = @Contact(
                        name = "Peter Nwaogu",
                        email = "peternwaogu05@gmail.com"
                ),
                version = "2.0"
        ),

        security = {
                @SecurityRequirement(name = "bearerAuth")
        }

)
@SecurityScheme(
        name = "bearerAuth",
        description = "Enter JWT token here",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {

//    Use @Operation to customize API endpoints for more clarity
//    Use @Hidden (Class / Endpoint level) to hide APIs from documentation
//    Use @Tag to rename API groups (swagger uses ur class / controller name by default)

}
