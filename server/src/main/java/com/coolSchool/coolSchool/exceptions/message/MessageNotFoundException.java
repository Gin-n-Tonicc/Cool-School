package com.coolSchool.coolSchool.exceptions.message;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class MessageNotFoundException extends ApiException {
    public MessageNotFoundException() {
        super("Message not found", HttpStatus.NOT_FOUND);
    }
}