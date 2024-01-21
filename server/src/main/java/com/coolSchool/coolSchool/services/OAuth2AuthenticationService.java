package com.coolSchool.coolSchool.services;

import com.coolSchool.coolSchool.security.CustomOAuth2User;
import jakarta.servlet.http.Cookie;

import java.util.function.Consumer;

public interface OAuth2AuthenticationService {
    boolean processOAuthPostLogin(CustomOAuth2User oAuth2User, Consumer<Cookie> addCookieFunc);
}
