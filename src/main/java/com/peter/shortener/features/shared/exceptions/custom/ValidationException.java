package com.peter.shortener.features.shared.exceptions.custom;

import com.peter.shortener.features.shared.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class ValidationException extends AppException {
    public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
