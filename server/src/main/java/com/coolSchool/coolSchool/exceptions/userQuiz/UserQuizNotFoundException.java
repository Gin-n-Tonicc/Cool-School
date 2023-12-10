package com.coolSchool.coolSchool.exceptions.userQuiz;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class UserQuizNotFoundException extends ApiException {
    public UserQuizNotFoundException() {
        super("UserQuiz not found", HttpStatus.NOT_FOUND);
    }
}
