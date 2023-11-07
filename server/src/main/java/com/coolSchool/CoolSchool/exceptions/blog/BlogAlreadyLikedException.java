package com.coolSchool.CoolSchool.exceptions.blog;

import com.coolSchool.CoolSchool.exceptions.common.BadRequestException;

public class BlogAlreadyLikedException extends BadRequestException {
    public BlogAlreadyLikedException() {
        super("Blog has already been liked!");
    }
}
