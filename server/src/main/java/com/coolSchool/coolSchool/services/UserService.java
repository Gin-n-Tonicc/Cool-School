package com.coolSchool.coolSchool.services;

import com.coolSchool.coolSchool.models.dto.auth.AdminUserDTO;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.auth.RegisterRequest;
import com.coolSchool.coolSchool.models.dto.request.CompleteOAuthRequest;
import com.coolSchool.coolSchool.models.entity.User;
import com.coolSchool.coolSchool.models.entity.VerificationToken;
import com.coolSchool.coolSchool.security.CustomOAuth2User;

import java.util.List;

public interface UserService {
    User createUser(RegisterRequest request);

    User findByEmail(String email);

    List<AdminUserDTO> getAllUsers();

    AdminUserDTO updateUser(Long id, AdminUserDTO userDTO, PublicUserDTO currentUser);

    void deleteUserById(Long id, PublicUserDTO currentUser);

    User processOAuthUser(CustomOAuth2User oAuth2User);

    User updateOAuth2UserWithFullData(CompleteOAuthRequest request, Long userId);

    User findById(Long id);

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);
}
