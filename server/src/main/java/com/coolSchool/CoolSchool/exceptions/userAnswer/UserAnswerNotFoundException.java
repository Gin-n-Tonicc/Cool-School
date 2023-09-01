package com.coolSchool.CoolSchool.exceptions.userAnswer;

import com.coolSchool.CoolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class UserAnswerNotFoundException extends ApiException {
    public UserAnswerNotFoundException() {
        super("UserAnswer not found", HttpStatus.NOT_FOUND);
    }
}
