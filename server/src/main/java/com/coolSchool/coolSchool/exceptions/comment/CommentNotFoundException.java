package com.coolSchool.coolSchool.exceptions.comment;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends ApiException {
    public CommentNotFoundException() {
        super("Comment not found", HttpStatus.NOT_FOUND);
    }
}
