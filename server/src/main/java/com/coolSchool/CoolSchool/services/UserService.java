package com.coolSchool.CoolSchool.services;

import com.coolSchool.CoolSchool.models.dto.auth.AdminUserDTO;
import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.CoolSchool.models.dto.auth.RegisterRequest;
import com.coolSchool.CoolSchool.models.entity.User;

import java.util.List;

public interface UserService {
    User createUser(RegisterRequest request);

    User findByEmail(String email);

    List<AdminUserDTO> getAllUsers();

    AdminUserDTO updateUser(Long id, AdminUserDTO userDTO, PublicUserDTO currentUser);

    void deleteUserById(Long id, PublicUserDTO currentUser);
}
