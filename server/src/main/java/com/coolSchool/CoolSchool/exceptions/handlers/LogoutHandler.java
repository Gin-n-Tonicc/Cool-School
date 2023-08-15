package com.coolSchool.CoolSchool.exceptions.handlers;

import com.coolSchool.CoolSchool.services.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutHandler implements org.springframework.security.web.authentication.logout.LogoutHandler {

  private final TokenService tokenService;

  @Override
  public void logout(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication
  ) {
    final String authHeader = request.getHeader("Authorization");

    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }

    final String jwt = authHeader.substring(7);
    tokenService.logoutToken(jwt);
  }
}
