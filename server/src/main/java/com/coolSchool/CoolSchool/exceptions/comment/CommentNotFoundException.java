package com.coolSchool.CoolSchool.exceptions.comment;

import com.coolSchool.CoolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends ApiException {
    public CommentNotFoundException() {
        super("Comment not found", HttpStatus.NOT_FOUND);
    }
}
