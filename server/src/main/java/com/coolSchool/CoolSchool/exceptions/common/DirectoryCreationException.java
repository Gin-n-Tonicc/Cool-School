package com.coolSchool.CoolSchool.exceptions.common;

import org.springframework.http.HttpStatus;

public class DirectoryCreationException extends ApiException {
    public DirectoryCreationException() {
        super("Error creating directory", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

