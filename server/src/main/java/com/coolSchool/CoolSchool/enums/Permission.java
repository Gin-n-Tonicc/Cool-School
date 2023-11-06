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
    TEACHER_READ("management:read"),
    TEACHER_UPDATE("management:update"),
    TEACHER_CREATE("management:create"),
    TEACHER_DELETE("management:delete");

    private final String permission;
}
