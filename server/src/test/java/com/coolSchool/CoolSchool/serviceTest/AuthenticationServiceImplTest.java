package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.coolSchool.enums.TokenType;
import com.coolSchool.coolSchool.exceptions.token.InvalidTokenException;
import com.coolSchool.coolSchool.models.dto.auth.AuthenticationRequest;
import com.coolSchool.coolSchool.models.dto.auth.AuthenticationResponse;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.auth.RegisterRequest;
import com.coolSchool.coolSchool.models.dto.request.CompleteOAuthRequest;
import com.coolSchool.coolSchool.models.entity.Token;
import com.coolSchool.coolSchool.models.entity.User;
import com.coolSchool.coolSchool.repositories.UserRepository;
import com.coolSchool.coolSchool.repositories.VerificationTokenRepository;
import com.coolSchool.coolSchool.services.AuthenticationService;
import com.coolSchool.coolSchool.services.JwtService;
import com.coolSchool.coolSchool.services.TokenService;
import com.coolSchool.coolSchool.services.UserService;
import com.coolSchool.coolSchool.services.impl.security.AuthenticationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.testng.Assert;

import java.io.IOException;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.testng.Assert.assertEquals;

class AuthenticationServiceImplTest {
    @Mock
    private UserService userService;

    @Mock
    private TokenService tokenService;

    @Mock
    private JwtService jwtService;
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private MessageSource messageSource;
    @Mock
    private VerificationTokenRepository verificationTokenRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationServiceImpl(
                userService,
                tokenService,
                jwtService,
                authenticationManager,
                modelMapper,
                messageSource,
                verificationTokenRepository,
                userRepository,
                passwordEncoder
        );
    }

    @Test
    void registerTest() {
        RegisterRequest registerRequest = new RegisterRequest();
        User user = new User();
        AuthenticationResponse expectedResponse = new AuthenticationResponse();

        when(userService.createUser(registerRequest)).thenReturn(user);
        when(tokenService.generateAuthResponse(user)).thenReturn(expectedResponse);

        AuthenticationResponse actualResponse = authenticationService.register(registerRequest);

        assertEquals(expectedResponse, actualResponse);
        verify(tokenService, times(1)).generateAuthResponse(user);
    }

    @Test
    void authenticateTest() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        User user = new User();
        String email = "test@example.com";
        authenticationRequest.setEmail(email);
        authenticationRequest.setPassword("password");

        when(userService.findByEmail(email)).thenReturn(user);
        when(jwtService.isTokenValid(anyString(), eq(user))).thenReturn(true);

        AuthenticationResponse actualResponse = authenticationService.authenticate(authenticationRequest);


        verify(tokenService, times(1)).revokeAllUserTokens(user);
        verify(tokenService, times(1)).generateAuthResponse(user);
    }


    @Test
    void refreshTokenTest() throws IOException {
        String refreshToken = "validRefreshToken";
        User user = new User();
        Token token = new Token();
        token.setTokenType(TokenType.REFRESH);

        when(jwtService.extractUsername(refreshToken)).thenReturn("user@example.com");
        when(userService.findByEmail("user@example.com")).thenReturn(user);
        when(tokenService.findByToken(refreshToken)).thenReturn(token);
        when(jwtService.isTokenValid(refreshToken, user)).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("newAccessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("newRefreshToken");

        AuthenticationResponse actualResponse = authenticationService.refreshToken(refreshToken);

        Assertions.assertNotNull(actualResponse);
        verify(tokenService, times(1)).revokeAllUserTokens(user);
        verify(tokenService, times(1)).saveToken(user, "newAccessToken", TokenType.ACCESS);
    }

    @Test
    void completeOAuth2Test() {
        CompleteOAuthRequest completeOAuthRequest = new CompleteOAuthRequest();
        PublicUserDTO publicUserDTO = new PublicUserDTO();
        publicUserDTO.setId(123L);

        User updatedUser = new User();
        updatedUser.setId(123L);

        when(userService.updateOAuth2UserWithFullData(completeOAuthRequest, 123L)).thenReturn(updatedUser);
        when(tokenService.generateAuthResponse(updatedUser)).thenReturn(new AuthenticationResponse());

        AuthenticationResponse actualResponse = authenticationService.completeOAuth2(completeOAuthRequest, publicUserDTO);

        Assertions.assertNotNull(actualResponse);
        verify(tokenService, times(1)).generateAuthResponse(updatedUser);
    }

    @Test
    void meTestInvalidJwtToken() {
        Assert.assertThrows(InvalidTokenException.class, () -> authenticationService.me(null));
        Assert.assertThrows(InvalidTokenException.class, () -> authenticationService.me(""));
    }

    @Test
    void meTestInvalidAccessToken() {
        when(tokenService.findByToken(any())).thenReturn(null);
        Assert.assertThrows(InvalidTokenException.class, () -> authenticationService.me("validJwtToken"));

        verify(tokenService, Mockito.times(1)).findByToken("validJwtToken");
    }

    @Test
    void meTestInvalidTokenValidity() {
        Token accessToken = new Token();
        accessToken.setToken("validJwtToken");
        when(tokenService.findByToken("validJwtToken")).thenReturn(accessToken);
        when(jwtService.isTokenValid("validJwtToken", accessToken.getUser())).thenThrow(JwtException.class);

        Assert.assertThrows(JwtException.class, () -> authenticationService.me("validJwtToken"));

        verify(tokenService, Mockito.times(1)).findByToken("validJwtToken");
        verify(jwtService, Mockito.times(1)).isTokenValid("validJwtToken", accessToken.getUser());
    }

    @Test
    void attachAuthCookiesTest() {
        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .accessToken("sampleAccessToken")
                .refreshToken("sampleRefreshToken")
                .build();

        Consumer cookieConsumer = mock(Consumer.class);

        authenticationService.attachAuthCookies(authenticationResponse, cookieConsumer);

        verify(tokenService, Mockito.times(1)).attachAuthCookies(eq(authenticationResponse), eq(cookieConsumer));
    }
}
