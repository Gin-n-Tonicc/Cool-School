package com.coolSchool.CoolSchool.exceptions.questions;

import com.coolSchool.CoolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class QuestionNotFoundException extends ApiException {
    public QuestionNotFoundException() {
        super("Question not found", HttpStatus.NOT_FOUND);
    }
}