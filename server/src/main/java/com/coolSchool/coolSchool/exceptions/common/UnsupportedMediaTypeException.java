package com.coolSchool.coolSchool.exceptions.common;

import org.springframework.http.HttpStatus;

public class UnsupportedMediaTypeException extends ApiException {
    public UnsupportedMediaTypeException(String message) {
        super(message, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
}