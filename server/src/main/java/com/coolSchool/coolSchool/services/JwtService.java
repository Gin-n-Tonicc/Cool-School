package com.coolSchool.coolSchool.services;

import com.coolSchool.coolSchool.models.dto.auth.AuthenticationResponse;
import com.coolSchool.coolSchool.models.entity.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(UserDetails userDetails);

    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);
}
