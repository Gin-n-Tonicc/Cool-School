package com.coolSchool.CoolSchool.exceptions.blog;

import com.coolSchool.CoolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class BlogNotFoundException extends ApiException {
    public BlogNotFoundException() {
        super("Blog not found", HttpStatus.NOT_FOUND);
    }
}
