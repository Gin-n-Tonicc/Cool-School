package com.coolSchool.CoolSchool.exceptions.token;

import com.coolSchool.CoolSchool.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends ApiException {
    public InvalidTokenException() {
        super("Invalid token", HttpStatus.BAD_REQUEST);
    }
}
