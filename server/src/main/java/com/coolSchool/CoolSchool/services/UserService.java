package com.coolSchool.CoolSchool.services;

import com.coolSchool.CoolSchool.models.dto.auth.RegisterRequest;
import com.coolSchool.CoolSchool.models.entity.User;

public interface UserService {
    User createUser(RegisterRequest request);

    User findByEmail(String email);
}
