package com.coolSchool.coolSchool.security;

import com.coolSchool.coolSchool.config.FrontendConfig;
import com.coolSchool.coolSchool.services.OAuth2AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final OAuth2AuthenticationService oAuth2AuthenticationService;
    private final FrontendConfig frontendConfig;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();

        boolean isAdditionalInfoRequired = oAuth2AuthenticationService.processOAuthPostLogin(oauthUser, response::addCookie);

        if (isAdditionalInfoRequired) {
            response.sendRedirect(frontendConfig.getFinishRegisterUrl());
        } else {
            response.sendRedirect(frontendConfig.getBaseUrl());
        }
    }
}