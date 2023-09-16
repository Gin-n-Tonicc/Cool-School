package com.coolSchool.CoolSchool.services;

import com.coolSchool.CoolSchool.models.dto.AuthenticationRequest;
import com.coolSchool.CoolSchool.models.dto.AuthenticationResponse;
import com.coolSchool.CoolSchool.models.dto.RefreshTokenBodyDTO;
import com.coolSchool.CoolSchool.models.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse refreshToken(RefreshTokenBodyDTO refreshTokenBodyDTO) throws IOException;
}
