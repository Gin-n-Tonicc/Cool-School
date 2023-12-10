package com.coolSchool.coolSchool.exceptions.category;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class CategoryCreateException extends ApiException {
    public CategoryCreateException(boolean isUnique) {
        super(
                isUnique
                        ? "Category with the same name already exists"
                        : "Invalid user data",
                HttpStatus.BAD_REQUEST
        );
    }
}
