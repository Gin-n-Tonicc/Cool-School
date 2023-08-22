package com.coolSchool.CoolSchool.services;

import com.coolSchool.CoolSchool.models.dto.RegisterRequest;
import com.coolSchool.CoolSchool.models.entity.User;

import java.util.Optional;

public interface UserService {
    User createUser(RegisterRequest request);

    User findByEmail(String email);
}
