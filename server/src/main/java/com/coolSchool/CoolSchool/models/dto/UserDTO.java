package com.coolSchool.CoolSchool.models.dto;

import com.coolSchool.CoolSchool.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String name;
    private String lastname;
    private String email;
    private String username;
    private String address;
    private Role role;
}
