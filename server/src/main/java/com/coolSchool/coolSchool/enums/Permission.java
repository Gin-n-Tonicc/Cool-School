package com.coolSchool.coolSchool.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration representing permissions available in the application.
 * Each permission has a corresponding string representation.
 * This class is used to define and access different permissions required for authorization purposes.
 */
@Getter
@RequiredArgsConstructor
public enum Permission {
    USER_READ("user:read"),
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    TEACHER_READ("teacher:read"),
    TEACHER_UPDATE("teacher:update"),
    TEACHER_CREATE("teacher:create"),
    TEACHER_DELETE("teacher:delete");

    private final String permission;
}
