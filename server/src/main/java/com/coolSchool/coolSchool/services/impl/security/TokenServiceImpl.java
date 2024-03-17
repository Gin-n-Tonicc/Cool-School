package com.coolSchool.coolSchool.services.impl.security;

import com.coolSchool.coolSchool.enums.TokenType;
import com.coolSchool.coolSchool.models.dto.auth.AuthenticationResponse;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.entity.Token;
import com.coolSchool.coolSchool.models.entity.User;
import com.coolSchool.coolSchool.repositories.TokenRepository;
import com.coolSchool.coolSchool.services.JwtService;
import com.coolSchool.coolSchool.services.TokenService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;

/**
 * Service implementation responsible for handling authentication tokens.
 */
@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    // Constants for authentication cookie keys
    public final static String AUTH_COOKIE_KEY_JWT = "COOL_SCHOOL_SESSION_JWT";
    public final static String AUTH_COOKIE_KEY_REFRESH = "COOL_SCHOOL_SESSION_REFRESH";
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    @Value("${spring.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;
    @Value("${spring.security.jwt.expiration}")
    private long jwtExpiration;

    @Override
    public Token findByToken(String jwt) {
        return tokenRepository.findByToken(jwt).orElse(null);
    }

    @Override
    public List<Token> findByUser(User user) {
        return tokenRepository.findAllByUser(user);
    }

    @Override
    public void saveToken(User user, String jwtToken, TokenType tokenType) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(tokenType)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

    @Override
    public void revokeToken(Token token) {
        tokenRepository.delete(token);
    }

    @Override
    public void revokeAllUserTokens(User user) {
        tokenRepository.deleteAll(tokenRepository.findAllByUser(user));
    }

    @Override
    @Transactional
    public void logoutToken(String jwt) {
        Token storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);

        if (storedToken == null) {
            return;
        }

        revokeAllUserTokens(storedToken.getUser());
        SecurityContextHolder.clearContext();
    }

    @Override
    public Cookie createJwtCookie(String jwt) {
        Cookie jwtCookie = new Cookie(AUTH_COOKIE_KEY_JWT, URLEncoder.encode(jwt, StandardCharsets.UTF_8));
        jwtCookie.setPath("/");

        // milliseconds to seconds
        jwtCookie.setMaxAge((int) jwtExpiration / 1000);
        jwtCookie.setSecure(true);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setAttribute("SameSite", "None");

        return jwtCookie;
    }

    @Override
    public Cookie createRefreshCookie(String refreshToken) {
        Cookie refreshCookie = new Cookie(AUTH_COOKIE_KEY_REFRESH, URLEncoder.encode(refreshToken, StandardCharsets.UTF_8));
        refreshCookie.setPath("/");

        // milliseconds to seconds
        refreshCookie.setMaxAge((int) refreshExpiration / 1000);
        refreshCookie.setSecure(true);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setAttribute("SameSite", "None");

        return refreshCookie;
    }

    @Override
    public AuthenticationResponse generateAuthResponse(User user) {
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        saveToken(user, jwtToken, TokenType.ACCESS);
        saveToken(user, refreshToken, TokenType.REFRESH);

        return AuthenticationResponse
                .builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(modelMapper.map(user, PublicUserDTO.class))
                .build();
    }

    @Override
    public void attachAuthCookies(AuthenticationResponse authenticationResponse, Consumer<Cookie> cookieConsumer) {
        Cookie jwtCookie = createJwtCookie(authenticationResponse.getAccessToken());
        Cookie refreshCookie = createRefreshCookie(authenticationResponse.getRefreshToken());

        // Accept cookies using the consumer
        cookieConsumer.accept(jwtCookie);
        cookieConsumer.accept(refreshCookie);
    }

    @Override
    public void detachAuthCookies(Consumer<Cookie> cookieConsumer) {
        Cookie jwtCookie = createJwtCookie("placeholder");
        Cookie refreshCookie = createRefreshCookie("placeholder");

        jwtCookie.setMaxAge(0);
        refreshCookie.setMaxAge(0);

        // Accept cookies using the consumer to clear existing cookies
        cookieConsumer.accept(jwtCookie);
        cookieConsumer.accept(refreshCookie);
    }
}
