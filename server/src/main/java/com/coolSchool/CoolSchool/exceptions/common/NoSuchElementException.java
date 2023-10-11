package com.coolSchool.CoolSchool.exceptions.common;

import org.springframework.http.HttpStatus;

public class NoSuchElementException extends ApiException {
    public NoSuchElementException() {
        super("NoSuchElementException", HttpStatus.NOT_FOUND);
    }
}
