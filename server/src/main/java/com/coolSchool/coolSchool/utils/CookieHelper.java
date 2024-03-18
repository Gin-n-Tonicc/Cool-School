package com.coolSchool.coolSchool.utils;

import jakarta.servlet.http.Cookie;

import java.util.Arrays;
import java.util.Optional;

public class CookieHelper {

    /**
     * Reads the value of a cookie with the specified key from an array of cookies.
     *
     * @param key     The key of the cookie to be read.
     * @param cookies An array of Cookie objects.
     * @return An Optional containing the value of the cookie if found, or Optional.empty() if not found.
     */
    public static Optional<String> readCookie(String key, Cookie[] cookies) {
        // If cookies array is null, return Optional.empty()
        if (cookies == null) {
            return Optional.empty();
        }

        // Stream through the array of cookies, filter by key, and map to the value
        return Arrays.stream(cookies)
                .filter(c -> key.equals(c.getName())) // Filter by key
                .map(Cookie::getValue) // Map to value
                .findAny(); // Find any matching cookie value
    }
}
