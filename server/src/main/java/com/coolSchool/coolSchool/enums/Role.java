package com.coolSchool.coolSchool.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.coolSchool.coolSchool.enums.Permission.*;

/**
 * Enumeration representing different roles in the application.
 * Each role has a set of associated permissions.
 * Roles in out application: ADMIN, TEACHER AND STUDENT
 */
@Getter
@RequiredArgsConstructor
public enum Role {

    USER(Collections.emptySet()),
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    TEACHER_READ,
                    TEACHER_UPDATE,
                    TEACHER_DELETE,
                    TEACHER_CREATE
            )
    ),
    TEACHER(
            Set.of(
                    TEACHER_READ,
                    TEACHER_UPDATE,
                    TEACHER_DELETE,
                    TEACHER_CREATE
            )
    );

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
