package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.CoolSchool.models.entity.Token;
import com.coolSchool.CoolSchool.models.entity.User;
import com.coolSchool.CoolSchool.repositories.TokenRepository;
import com.coolSchool.CoolSchool.services.impl.TokenServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Test
    void testSaveToken() {
        User user = new User();
        String jwtToken = "mockedJwtToken";

        tokenService.saveToken(user, jwtToken);

        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void testRevokeAllUserTokens() {
        User user = new User();
        Token token1 = new Token();
        Token token2 = new Token();

        when(tokenRepository.findAllByUser(user)).thenReturn(Arrays.asList(token1, token2));

        tokenService.revokeAllUserTokens(user);

        verify(tokenRepository, times(1)).deleteAll(anyList());
    }

    @Test
    void testLogoutToken() {
        String jwtToken = "mockedJwtToken";
        User user = new User();
        Token storedToken = new Token();
        storedToken.setUser(user);

        when(tokenRepository.findByToken(jwtToken)).thenReturn(Optional.of(storedToken));

        tokenService.logoutToken(jwtToken);

        verify(tokenRepository, times(1)).findByToken(jwtToken);
        verify(tokenRepository, times(1)).findAllByUser(user);
        verify(tokenRepository, times(1)).deleteAll(anyList());
    }

    @Test
    void testLogoutToken_NoTokenFound() {
        String jwtToken = "nonExistentJwtToken";

        when(tokenRepository.findByToken(jwtToken)).thenReturn(Optional.empty());

        tokenService.logoutToken(jwtToken);

        verify(tokenRepository, never()).findAllByUser(any(User.class));
        verify(tokenRepository, never()).deleteAll(anyList());
    }
}
