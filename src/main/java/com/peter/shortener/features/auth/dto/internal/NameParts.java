package com.peter.shortener.features.auth.dto.internal;

public record NameParts(
        String firstName,
        String lastName
) {
    public static NameParts names(String firstName, String lastName){
        return new NameParts(firstName, lastName);
    }
}
