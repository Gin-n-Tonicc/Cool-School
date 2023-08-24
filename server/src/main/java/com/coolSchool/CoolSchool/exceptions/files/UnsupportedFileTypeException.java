package com.coolSchool.CoolSchool.exceptions.files;

import com.coolSchool.CoolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class UnsupportedFileTypeException extends ApiException {
    public UnsupportedFileTypeException() {
        super("Unsupported file type exception", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
}
