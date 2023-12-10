package com.coolSchool.coolSchool.exceptions.resource;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException() {
        super("Resource not found", HttpStatus.NOT_FOUND);
    }
}