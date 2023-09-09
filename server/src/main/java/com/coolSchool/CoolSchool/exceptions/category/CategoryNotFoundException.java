package com.coolSchool.CoolSchool.exceptions.category;

import com.coolSchool.CoolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends ApiException {
    public CategoryNotFoundException() {
        super("Category not found", HttpStatus.NOT_FOUND);
    }
}