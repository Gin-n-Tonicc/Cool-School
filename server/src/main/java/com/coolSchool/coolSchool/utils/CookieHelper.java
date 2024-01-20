package com.coolSchool.coolSchool.utils;

import jakarta.servlet.http.Cookie;

import java.util.Arrays;
import java.util.Optional;

public class CookieHelper {
    public static Optional<String> readCookie(String key, Cookie[] cookies) {
        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(c -> key.equals(c.getName()))
                .map(Cookie::getValue)
                .findAny();
    }
}
