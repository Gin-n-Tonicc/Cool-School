package com.coolSchool.CoolSchool.exceptions.quizzes;

import com.coolSchool.CoolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class QuizNotFoundException extends ApiException {
    public QuizNotFoundException() {
        super("Quiz not found", HttpStatus.NOT_FOUND);
    }
}