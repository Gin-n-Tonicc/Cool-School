package com.coolSchool.coolSchool.exceptions.blog;

import com.coolSchool.coolSchool.exceptions.common.BadRequestException;

public class BlogAlreadyLikedException extends BadRequestException {
    public BlogAlreadyLikedException() {
        super("Blog has already been liked!");
    }
}
