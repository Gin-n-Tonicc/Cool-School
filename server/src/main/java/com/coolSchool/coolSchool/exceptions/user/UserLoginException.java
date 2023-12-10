package com.coolSchool.coolSchool.exceptions.user;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class UserLoginException extends ApiException {
    public UserLoginException() {
        super("Invalid email or password", HttpStatus.BAD_REQUEST);
    }
}
