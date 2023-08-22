package com.coolSchool.CoolSchool.services.impl;

import com.coolSchool.CoolSchool.models.dto.AuthenticationRequest;
import com.coolSchool.CoolSchool.models.dto.AuthenticationResponse;
import com.coolSchool.CoolSchool.models.dto.RegisterRequest;
import com.coolSchool.CoolSchool.models.entity.User;
import com.coolSchool.CoolSchool.services.AuthenticationService;
import com.coolSchool.CoolSchool.services.JwtService;
import com.coolSchool.CoolSchool.services.TokenService;
import com.coolSchool.CoolSchool.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
  private final UserService userService;
  private final TokenService tokenService;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @Override
  public AuthenticationResponse register(RegisterRequest request) {
    User user = userService.createUser(request);
    String jwtToken = jwtService.generateToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);

    tokenService.saveToken(user, jwtToken);

    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  @Override
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );

    User user = userService.findByEmail(request.getEmail());

    String jwtToken = jwtService.generateToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);

    tokenService.revokeAllUserTokens(user);
    tokenService.saveToken(user, jwtToken);

    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  @Override
  public AuthenticationResponse refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return null;
    }

    final String refreshToken = authHeader.substring(7);
    final String userEmail = jwtService.extractUsername(refreshToken);

    if (userEmail == null) {
      return null;
    }

    User user = userService.findByEmail(userEmail);

    if (!jwtService.isTokenValid(refreshToken, user)) {
      return null;
    }

    String accessToken = jwtService.generateToken(user);
    tokenService.revokeAllUserTokens(user);
    tokenService.saveToken(user, accessToken);

    return AuthenticationResponse.builder()
          .accessToken(accessToken)
          .refreshToken(refreshToken)
          .build();
  }
}
