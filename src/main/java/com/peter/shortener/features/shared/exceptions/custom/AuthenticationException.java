package com.peter.shortener.features.shared.exceptions.custom;

import com.peter.shortener.features.shared.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class AuthenticationException extends AppException {
    public AuthenticationException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
