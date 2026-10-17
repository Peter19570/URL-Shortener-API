package com.peter.shortener.features.auth.dto.response;

import com.peter.shortener.features.user.dto.response.UserResponse;

public record AuthResponse(
        Boolean authenticated,
        TokenResponse token,
        UserResponse userInfo
) {}
