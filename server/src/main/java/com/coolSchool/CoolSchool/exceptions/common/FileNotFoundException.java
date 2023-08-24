package com.coolSchool.CoolSchool.exceptions.common;

import org.springframework.http.HttpStatus;

public class FileNotFoundException extends ApiException {
    public FileNotFoundException() {
        super("File not found", HttpStatus.NOT_FOUND);
    }
}
