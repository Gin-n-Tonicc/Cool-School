package com.coolSchool.coolSchool.exceptions.common;

import org.springframework.http.HttpStatus;

public class NoSuchElementException extends ApiException {
    public NoSuchElementException() {
        super("NoSuchElementException", HttpStatus.NOT_FOUND);
    }
}
