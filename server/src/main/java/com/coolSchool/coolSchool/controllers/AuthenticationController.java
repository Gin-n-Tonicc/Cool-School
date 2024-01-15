package com.coolSchool.coolSchool.controllers;

import com.coolSchool.coolSchool.models.dto.auth.AuthenticationRequest;
import com.coolSchool.coolSchool.models.dto.auth.AuthenticationResponse;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.auth.RegisterRequest;
import com.coolSchool.coolSchool.services.AuthenticationService;
import com.coolSchool.coolSchool.utils.CookieHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.coolSchool.coolSchool.services.impl.security.TokenServiceImpl.AUTH_COOKIE_KEY_JWT;
import static com.coolSchool.coolSchool.services.impl.security.TokenServiceImpl.AUTH_COOKIE_KEY_REFRESH;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<PublicUserDTO> register(@RequestBody RegisterRequest request, HttpServletResponse servletResponse) {
        AuthenticationResponse authenticationResponse = authenticationService.register(request);
        authenticationService.attachAuthCookies(authenticationResponse, servletResponse::addCookie);

        return ResponseEntity.ok(authenticationResponse.getUser());
    }

    @PostMapping("/authenticate")
    public ResponseEntity<PublicUserDTO> authenticate(@RequestBody AuthenticationRequest request, HttpServletResponse servletResponse) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);
        authenticationService.attachAuthCookies(authenticationResponse, servletResponse::addCookie);

        return ResponseEntity.ok(authenticationResponse.getUser());
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<PublicUserDTO> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = CookieHelper.readCookie(AUTH_COOKIE_KEY_REFRESH, request.getCookies()).orElse(null);

        AuthenticationResponse authenticationResponse = authenticationService.refreshToken(refreshToken);
        authenticationService.attachAuthCookies(authenticationResponse, response::addCookie);

        return ResponseEntity.ok(authenticationResponse.getUser());
    }

    @GetMapping("/me")
    public ResponseEntity<PublicUserDTO> getMe(HttpServletRequest request, HttpServletResponse response) {
        String jwtToken = CookieHelper.readCookie(AUTH_COOKIE_KEY_JWT, request.getCookies()).orElse(null);

        AuthenticationResponse authenticationResponse = authenticationService.me(jwtToken);
        authenticationService.attachAuthCookies(authenticationResponse, response::addCookie);

        return ResponseEntity.ok(authenticationResponse.getUser());
    }
}
