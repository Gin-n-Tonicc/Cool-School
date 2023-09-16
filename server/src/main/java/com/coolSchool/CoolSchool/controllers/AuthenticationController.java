package com.coolSchool.CoolSchool.controllers;

import com.coolSchool.CoolSchool.models.dto.*;
import com.coolSchool.CoolSchool.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @RequestBody RefreshTokenBodyDTO refreshTokenBody
            ) throws IOException {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenBody));
    }

    @PostMapping("/me")
    public ResponseEntity<AuthenticationResponse> getMe(
            @RequestBody AccessTokenBodyDTO accessTokenBodyDTO
            ) {
        return ResponseEntity.ok(authenticationService.me(accessTokenBodyDTO));
    }
}
