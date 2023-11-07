package com.coolSchool.CoolSchool.exceptions.review;

import com.coolSchool.CoolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class ReviewNotFoundException extends ApiException {
    public ReviewNotFoundException() {
        super("Review not found", HttpStatus.NOT_FOUND);
    }
}