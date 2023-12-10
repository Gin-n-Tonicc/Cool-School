package com.coolSchool.coolSchool.services;

import com.coolSchool.coolSchool.models.dto.auth.*;

import java.io.IOException;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse refreshToken(RefreshTokenBodyDTO refreshTokenBodyDTO) throws IOException;

    AuthenticationResponse me(
            AccessTokenBodyDTO accessTokenBodyDTO
    );
}
