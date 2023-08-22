package com.coolSchool.CoolSchool.exceptions.user;

import com.coolSchool.CoolSchool.exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class UserLoginException extends ApiException {
    public UserLoginException() {
        super("Invalid email or password", HttpStatus.BAD_REQUEST);
    }
}
