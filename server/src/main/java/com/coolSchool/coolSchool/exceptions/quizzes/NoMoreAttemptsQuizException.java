package com.coolSchool.coolSchool.exceptions.quizzes;

import com.coolSchool.coolSchool.exceptions.common.BadRequestException;

public class NoMoreAttemptsQuizException extends BadRequestException {
    public NoMoreAttemptsQuizException() {
        super("No more attempts are possible for this quiz!");
    }
}
