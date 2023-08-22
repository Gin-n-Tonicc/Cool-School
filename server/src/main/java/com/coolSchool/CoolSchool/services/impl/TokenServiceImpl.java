package com.coolSchool.CoolSchool.services.impl;

import com.coolSchool.CoolSchool.enums.TokenType;
import com.coolSchool.CoolSchool.exceptions.common.AccessDeniedException;
import com.coolSchool.CoolSchool.exceptions.token.InvalidTokenException;
import com.coolSchool.CoolSchool.models.entity.Token;
import com.coolSchool.CoolSchool.models.entity.User;
import com.coolSchool.CoolSchool.repositories.TokenRepository;
import com.coolSchool.CoolSchool.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    @Override
    public void saveToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
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
}
