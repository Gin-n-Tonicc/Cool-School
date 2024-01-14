package com.coolSchool.coolSchool.security;

import com.coolSchool.coolSchool.config.FrontendConfig;
import com.coolSchool.coolSchool.services.AuthenticationService;
import com.coolSchool.coolSchool.services.OAuth2AuthenticationService;
import com.coolSchool.coolSchool.services.UserService;
import com.coolSchool.coolSchool.utils.UriHelper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final OAuth2AuthenticationService oAuth2AuthenticationService;
    private final FrontendConfig frontendConfig;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();

        String accessToken = oAuth2AuthenticationService.processOAuthPostLogin(oauthUser, response::addCookie);
        URI uri = UriHelper.appendUri(frontendConfig.getLoginUrl(), "session=" + accessToken);
        response.sendRedirect(uri.toString());
    }
}