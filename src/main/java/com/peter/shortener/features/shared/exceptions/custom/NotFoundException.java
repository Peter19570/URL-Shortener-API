package com.peter.shortener.features.shared.exceptions.custom;

import com.peter.shortener.features.shared.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends AppException {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
