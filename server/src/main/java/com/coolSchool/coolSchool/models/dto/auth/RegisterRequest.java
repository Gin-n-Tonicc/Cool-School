package com.coolSchool.coolSchool.models.dto.auth;

import com.coolSchool.coolSchool.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String address;
    private String username;
    private String description;
    private Role role;
    private boolean additionalInfoRequired;
}
