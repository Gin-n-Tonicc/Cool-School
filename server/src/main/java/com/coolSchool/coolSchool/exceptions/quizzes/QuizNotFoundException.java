package com.coolSchool.coolSchool.exceptions.quizzes;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class QuizNotFoundException extends ApiException {
    public QuizNotFoundException() {
        super("Quiz not found", HttpStatus.NOT_FOUND);
    }
}