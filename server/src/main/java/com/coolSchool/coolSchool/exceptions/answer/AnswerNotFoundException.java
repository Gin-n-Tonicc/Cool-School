package com.coolSchool.coolSchool.exceptions.answer;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class AnswerNotFoundException extends ApiException {
    public AnswerNotFoundException() {
        super("Answer not found", HttpStatus.NOT_FOUND);
    }
}
