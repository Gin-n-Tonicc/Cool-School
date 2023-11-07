package com.coolSchool.CoolSchool.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
