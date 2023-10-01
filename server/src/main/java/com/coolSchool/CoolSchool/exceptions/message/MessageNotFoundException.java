package com.coolSchool.CoolSchool.exceptions.message;

import com.coolSchool.CoolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class MessageNotFoundException extends ApiException {
    public MessageNotFoundException() {
        super("Message not found", HttpStatus.NOT_FOUND);
    }
}