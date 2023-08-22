package com.coolSchool.CoolSchool.exceptions;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends ApiException {
    public InternalServerErrorException() {
        super("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
