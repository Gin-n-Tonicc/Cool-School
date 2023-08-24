package com.coolSchool.CoolSchool.exceptions.common;

import org.springframework.http.HttpStatus;

public class UnsupportedFileTypeException extends ApiException {
    public UnsupportedFileTypeException() {
        super("Unsupported file type exception", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
}
