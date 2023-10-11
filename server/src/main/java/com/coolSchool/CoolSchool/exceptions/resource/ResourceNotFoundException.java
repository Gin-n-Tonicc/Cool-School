package com.coolSchool.CoolSchool.exceptions.resource;

import com.coolSchool.CoolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException() {
        super("Resource not found", HttpStatus.NOT_FOUND);
    }
}