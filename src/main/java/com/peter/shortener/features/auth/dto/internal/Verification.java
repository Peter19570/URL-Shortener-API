package com.peter.shortener.features.auth.dto.internal;

public record Verification(
        String userId,
        String newEmail
) {
    public static Verification data(String userId, String newEmail){
        return new Verification(userId, newEmail);
    }
}
