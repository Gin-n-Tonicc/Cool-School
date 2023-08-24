package com.coolSchool.CoolSchool.services;

import com.coolSchool.CoolSchool.models.entity.Token;
import com.coolSchool.CoolSchool.models.entity.User;

public interface TokenService {
    Token findByToken(String jwt);
    void saveToken(User user, String jwtToken);

    void revokeAllUserTokens(User user);

    void logoutToken(String jwt);
}
