package com.coolSchool.CoolSchool.exceptions.common;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ApiException {
    public BadRequestException() {
        super("BadRequestException", HttpStatus.BAD_REQUEST);
    }
}
