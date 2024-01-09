package com.coolSchool.coolSchool.security;

import com.coolSchool.coolSchool.config.FrontendConfig;
import com.coolSchool.coolSchool.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final UserService userService;
    private final FrontendConfig frontendConfig;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        System.out.println("AuthenticationSuccessHandler invoked");
        System.out.println("Authentication username: " + authentication.getName());
        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();

        userService.processOAuthPostLogin(oauthUser);
        response.addCookie(createCookie(oauthUser));
        response.sendRedirect(frontendConfig.getLoginUrl());
    }

    private Cookie createCookie(CustomOAuth2User oauthUser) {
        Cookie cookie = new Cookie("my_cookie", URLEncoder.encode(oauthUser.getName(), StandardCharsets.UTF_8));
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(70000);
        return cookie;
    }
}