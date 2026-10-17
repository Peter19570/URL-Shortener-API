package com.peter.shortener.features.shared.exceptions.custom;

import com.peter.shortener.features.shared.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class AlreadyExistException extends AppException {
    public AlreadyExistException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
