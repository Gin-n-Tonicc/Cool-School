package com.coolSchool.coolSchool.exceptions.common;

import org.springframework.http.HttpStatus;

public class IllegalArgumentApiException extends ApiException {

    public IllegalArgumentApiException() {
        super("Illegal Argument", HttpStatus.BAD_REQUEST);
    }
}
