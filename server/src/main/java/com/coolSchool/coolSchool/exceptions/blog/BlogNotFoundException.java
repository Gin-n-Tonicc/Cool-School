package com.coolSchool.coolSchool.exceptions.blog;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class BlogNotFoundException extends ApiException {
    public BlogNotFoundException() {
        super("Blog not found", HttpStatus.NOT_FOUND);
    }
}
