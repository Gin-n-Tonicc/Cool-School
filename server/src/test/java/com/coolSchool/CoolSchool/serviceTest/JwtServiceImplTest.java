package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.coolSchool.services.impl.security.JwtServiceImpl;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceImplTest {
    @InjectMocks
    private JwtServiceImpl jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        String secretKey = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
        long jwtExpiration = 86400000;
        long refreshExpiration = 604800000;
        jwtService = new JwtServiceImpl(secretKey, jwtExpiration, refreshExpiration);
    }

    @Test
    void testExtractUsername() {
        String token = jwtService.generateToken(createUserDetails());
        String username = jwtService.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void testExtractClaim() {
        String token = jwtService.generateToken(createUserDetails());
        Claims claims = jwtService.extractAllClaims(token);
        assertNotNull(claims);
        assertEquals("testuser", claims.getSubject());
    }

    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken(createUserDetails());
        assertNotNull(token);
    }

    @Test
    void testGenerateTokenWithExtraClaims() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("customClaim", "customValue");
        String token = jwtService.generateToken(extraClaims, createUserDetails());
        assertNotNull(token);
    }

    @Test
    void testGenerateRefreshToken() {
        String refreshToken = jwtService.generateRefreshToken(createUserDetails());
        assertNotNull(refreshToken);
    }

    @Test
    void testIsTokenValidWithValidToken() {
        String token = jwtService.generateToken(createUserDetails());
        assertTrue(jwtService.isTokenValid(token, createUserDetails()));
    }

    private UserDetails createUserDetails() {
        return User.withUsername("testuser")
                .password("password")
                .roles("USER")
                .build();
    }

}