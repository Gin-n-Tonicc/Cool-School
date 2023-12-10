package com.coolSchool.coolSchool.exceptions.questions;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class QuestionNotFoundException extends ApiException {
    public QuestionNotFoundException() {
        super("Question not found", HttpStatus.NOT_FOUND);
    }
}