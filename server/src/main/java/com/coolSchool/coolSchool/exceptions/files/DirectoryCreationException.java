package com.coolSchool.coolSchool.exceptions.files;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class DirectoryCreationException extends ApiException {
    public DirectoryCreationException() {
        super("Error creating directory", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

