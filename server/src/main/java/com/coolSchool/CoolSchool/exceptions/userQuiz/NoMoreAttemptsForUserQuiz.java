package com.coolSchool.CoolSchool.exceptions.userQuiz;

import com.coolSchool.CoolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class NoMoreAttemptsForUserQuiz extends ApiException {
    public NoMoreAttemptsForUserQuiz() {
        super("No more attempts are possible!", HttpStatus.BAD_REQUEST);
    }
}
