package com.coolSchool.coolSchool.services.impl.security;

import com.coolSchool.coolSchool.enums.TokenType;
import com.coolSchool.coolSchool.exceptions.token.InvalidTokenException;
import com.coolSchool.coolSchool.exceptions.user.UserLoginException;
import com.coolSchool.coolSchool.models.dto.auth.*;
import com.coolSchool.coolSchool.models.entity.Token;
import com.coolSchool.coolSchool.models.entity.User;
import com.coolSchool.coolSchool.services.AuthenticationService;
import com.coolSchool.coolSchool.services.JwtService;
import com.coolSchool.coolSchool.services.TokenService;
import com.coolSchool.coolSchool.services.UserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;

import java.util.List;
import java.util.function.Consumer;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final TokenService tokenService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        User user = userService.createUser(request);
        return tokenService.generateAuthResponse(user);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException exception) {
            throw new UserLoginException();
        }

        User user = userService.findByEmail(request.getEmail());
        tokenService.revokeAllUserTokens(user);

        return tokenService.generateAuthResponse(user);
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenBodyDTO refreshTokenBodyDTO) {
        final String refreshToken = refreshTokenBodyDTO.getRefreshToken();

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new InvalidTokenException();
        }

        String userEmail;

        try {
            userEmail = jwtService.extractUsername(refreshToken);
        } catch (JwtException exception) {
            throw new InvalidTokenException();
        }

        if (userEmail == null) {
            throw new InvalidTokenException();
        }

        // Make sure token is a refresh token not an access token
        Token token = tokenService.findByToken(refreshToken);
        if (token != null && token.tokenType != TokenType.REFRESH) {
            throw new InvalidTokenException();
        }

        User user = userService.findByEmail(userEmail);

        if (!jwtService.isTokenValid(refreshToken, user)) {
            tokenService.revokeToken(token);
            throw new InvalidTokenException();
        }

        String accessToken = jwtService.generateToken(user);

        tokenService.revokeAllUserTokens(user);
        tokenService.saveToken(user, accessToken, TokenType.ACCESS);
        tokenService.saveToken(user, refreshToken, TokenType.REFRESH);

        return AuthenticationResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponse me(AccessTokenBodyDTO accessTokenBodyDTO) {
        Token accessToken = tokenService.findByToken(accessTokenBodyDTO.getAccessToken());

        if (accessToken == null) {
            throw new InvalidTokenException();
        }

        User user = accessToken.getUser();

        boolean isTokenValid;

        try {
            isTokenValid = jwtService.isTokenValid(accessToken.getToken(), user);
        } catch (JwtException jwtException) {
            isTokenValid = false;
        }

        if (!isTokenValid) {
            tokenService.revokeAllUserTokens(user);
            throw new InvalidTokenException();
        }

        List<Token> tokens = tokenService.findByUser(user);
        Token refreshToken = tokens.stream().filter(x -> x.getTokenType() == TokenType.REFRESH).toList().get(0);

        if (refreshToken == null) {
            throw new InvalidTokenException();
        }

        String refreshTokenString;

        if (!jwtService.isTokenValid(refreshToken.getToken(), user)) {
            refreshTokenString = jwtService.generateRefreshToken(user);
            tokenService.saveToken(user, refreshTokenString, TokenType.REFRESH);
        } else {
            refreshTokenString = refreshToken.getToken();
        }

        PublicUserDTO publicUser = modelMapper.map(accessToken.getUser(), PublicUserDTO.class);

        return AuthenticationResponse
                .builder()
                .accessToken(accessToken.getToken())
                .refreshToken(refreshTokenString)
                .user(publicUser)
                .build();
    }

    @Override
    public void attachAuthCookies(AuthenticationResponse authenticationResponse, Consumer<Cookie> cookieConsumer) {
        tokenService.attachAuthCookies(authenticationResponse, cookieConsumer);
    }
}
