package com.coolSchool.coolSchool.controllers;

import com.coolSchool.coolSchool.config.FrontendConfig;
import com.coolSchool.coolSchool.exceptions.answer.filters.JwtAuthenticationFilter;
import com.coolSchool.coolSchool.exceptions.email.EmailNotVerified;
import com.coolSchool.coolSchool.exceptions.user.UserNotFoundException;
import com.coolSchool.coolSchool.interfaces.RateLimited;
import com.coolSchool.coolSchool.models.dto.auth.AuthenticationRequest;
import com.coolSchool.coolSchool.models.dto.auth.AuthenticationResponse;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.auth.RegisterRequest;
import com.coolSchool.coolSchool.models.dto.request.CompleteOAuthRequest;
import com.coolSchool.coolSchool.models.entity.User;
import com.coolSchool.coolSchool.models.entity.VerificationToken;
import com.coolSchool.coolSchool.repositories.UserRepository;
import com.coolSchool.coolSchool.repositories.VerificationTokenRepository;
import com.coolSchool.coolSchool.services.AuthenticationService;
import com.coolSchool.coolSchool.services.impl.security.events.OnPasswordResetRequestEvent;
import com.coolSchool.coolSchool.services.impl.security.events.OnRegistrationCompleteEvent;
import com.coolSchool.coolSchool.utils.CookieHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;

import static com.coolSchool.coolSchool.services.impl.security.TokenServiceImpl.AUTH_COOKIE_KEY_JWT;
import static com.coolSchool.coolSchool.services.impl.security.TokenServiceImpl.AUTH_COOKIE_KEY_REFRESH;

/**
 * Controller class for handling authentication-related operations.
 * JWT (access and refresh token);
 * OAuth2;
 * Email confirmation;
 * Forgotten password.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final ApplicationEventPublisher eventPublisher;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MessageSource messageSource;
    private final FrontendConfig frontendConfig;

    @Value("${server.backend.baseUrl}")
    private String appBaseUrl;

    @RateLimited
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        AuthenticationResponse authenticationResponse = authenticationService.register(request);
        sendVerificationEmail(modelMapper.map(authenticationResponse.getUser(), User.class));
        return ResponseEntity.ok(authenticationResponse);
    }

    //Endpoint for email confirmation during registration
    @GetMapping("/registrationConfirm")
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token, HttpServletResponse httpServletResponse) throws IOException {

        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expired!");
        }
        verificationToken.setCreatedAt(LocalDateTime.now());

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expired!");
        }

        user.setEnabled(true);
        userRepository.save(user);
        httpServletResponse.sendRedirect(frontendConfig.getLoginUrl());
        return ResponseEntity.ok("User registration confirmed successfully!");
    }

    @RateLimited
    @PostMapping("/authenticate") // login
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request, HttpServletResponse servletResponse) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(messageSource));

        if (!user.isEnabled()) {
            throw new EmailNotVerified(messageSource);
        }

        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);
        authenticationService.attachAuthCookies(authenticationResponse, servletResponse::addCookie);

        return ResponseEntity.ok(authenticationResponse);
    }

    @RateLimited
    @PutMapping("/complete-oauth")
    // After registering with Google we need more information about the user, described in CompleteOAuthRequest
    public ResponseEntity<AuthenticationResponse> completeOAuth(@RequestBody CompleteOAuthRequest request, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        PublicUserDTO currentLoggedUser = (PublicUserDTO) servletRequest.getAttribute(JwtAuthenticationFilter.userKey);

        AuthenticationResponse authenticationResponse = authenticationService.completeOAuth2(request, currentLoggedUser);
        authenticationService.attachAuthCookies(authenticationResponse, servletResponse::addCookie);

        return ResponseEntity.ok(authenticationResponse);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = CookieHelper.readCookie(AUTH_COOKIE_KEY_REFRESH, request.getCookies()).orElse(null);

        AuthenticationResponse authenticationResponse = authenticationService.refreshToken(refreshToken);
        authenticationService.attachAuthCookies(authenticationResponse, response::addCookie);

        return ResponseEntity.ok(authenticationResponse);
    }

    @GetMapping("/me") // Retrieves current user information.
    public ResponseEntity<AuthenticationResponse> getMe(HttpServletRequest request, HttpServletResponse response) {
        String jwtToken = CookieHelper.readCookie(AUTH_COOKIE_KEY_JWT, request.getCookies()).orElse(null);

        AuthenticationResponse authenticationResponse = authenticationService.me(jwtToken);
        authenticationService.attachAuthCookies(authenticationResponse, response::addCookie);

        return ResponseEntity.ok(authenticationResponse);
    }

    private void sendVerificationEmail(User user) {
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, appBaseUrl));
    }

    @RateLimited
    @PostMapping("/forgot-password") // Sends link to email so the user can change their password
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(messageSource));
        eventPublisher.publishEvent(new OnPasswordResetRequestEvent(user, appBaseUrl));
        return ResponseEntity.ok("Password reset link sent to your email!");
    }

    @RateLimited
    @PostMapping("/password-reset")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword) {
        authenticationService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password reset successfully");
    }
}
