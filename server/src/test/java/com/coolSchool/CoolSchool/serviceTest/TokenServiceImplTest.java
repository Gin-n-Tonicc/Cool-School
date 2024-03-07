package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.coolSchool.enums.TokenType;
import com.coolSchool.coolSchool.models.entity.Token;
import com.coolSchool.coolSchool.models.entity.User;
import com.coolSchool.coolSchool.repositories.TokenRepository;
import com.coolSchool.coolSchool.services.JwtService;
import com.coolSchool.coolSchool.services.impl.security.TokenServiceImpl;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.assertEquals;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenServiceImpl tokenService;
    @Mock
    private JwtService jwtService;

    @Test
    public void testSaveToken() {
        User mockUser = new User();
        String jwtToken = "mockJwtToken";
        TokenType tokenType = TokenType.ACCESS;

        tokenService.saveToken(mockUser, jwtToken, tokenType);

        verify(tokenRepository).save(Mockito.argThat(token ->
                token.getUser() == mockUser &&
                        token.getToken().equals(jwtToken) &&
                        token.getTokenType() == tokenType &&
                        !token.isExpired() &&
                        !token.isRevoked()
        ));
    }

    @Test
    public void testFindByToken() {
        String jwt = "mockJwt";
        Token mockToken = new Token();
        when(tokenRepository.findByToken(jwt)).thenReturn(java.util.Optional.of(mockToken));
        Token result = tokenService.findByToken(jwt);
        assertEquals(mockToken, result);
    }

    @Test
    public void testFindByUser() {
        User mockUser = new User();
        Token mockToken1 = new Token();
        Token mockToken2 = new Token();

        when(tokenRepository.findAllByUser(mockUser)).thenReturn(List.of(mockToken1, mockToken2));
        List<Token> result = tokenService.findByUser(mockUser);
        assertEquals(2, result.size());
        assertEquals(mockToken1, result.get(0));
        assertEquals(mockToken2, result.get(1));
    }


    @Test
    public void testRevokeToken() {
        Token mockToken = new Token();
        tokenService.revokeToken(mockToken);
        verify(tokenRepository).delete(mockToken);
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

    @Test
    void createJwtCookieTest() {
        String jwtToken = "sampleJwtToken";
        Cookie jwtCookie = tokenService.createJwtCookie(jwtToken);

        Assertions.assertNotNull(jwtCookie);
        Assertions.assertEquals(TokenServiceImpl.AUTH_COOKIE_KEY_JWT, jwtCookie.getName());
        Assertions.assertEquals(URLEncoder.encode(jwtToken, StandardCharsets.UTF_8), jwtCookie.getValue());
        Assertions.assertEquals("/", jwtCookie.getPath());
    }

    @Test
    void createRefreshCookieTest() {
        String refreshToken = "sampleRefreshToken";
        Cookie refreshCookie = tokenService.createRefreshCookie(refreshToken);

        Assertions.assertNotNull(refreshCookie);
        Assertions.assertEquals(TokenServiceImpl.AUTH_COOKIE_KEY_REFRESH, refreshCookie.getName());
        Assertions.assertEquals(URLEncoder.encode(refreshToken, StandardCharsets.UTF_8), refreshCookie.getValue());
        Assertions.assertEquals("/", refreshCookie.getPath());

    }

    @Test
    void detachAuthCookiesTest() {
        Consumer<Cookie> cookieConsumer = mock(Consumer.class);

        tokenService.detachAuthCookies(cookieConsumer);

        verify(cookieConsumer, times(1)).accept(argThat(cookie ->
                "COOL_SCHOOL_SESSION_JWT".equals(cookie.getName()) &&
                        "placeholder".equals(cookie.getValue()) &&
                        cookie.getMaxAge() == 0
        ));

        verify(cookieConsumer, times(1)).accept(argThat(cookie ->
                "COOL_SCHOOL_SESSION_REFRESH".equals(cookie.getName()) &&
                        "placeholder".equals(cookie.getValue()) &&
                        cookie.getMaxAge() == 0
        ));
    }
}
