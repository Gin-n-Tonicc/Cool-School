package com.coolSchool.CoolSchool.models.dto.request;

import com.coolSchool.CoolSchool.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    private String name;
    @Size(min = 5)
    @NotNull(message = "Enter the last name of the user")
    private String lastname;
    @Email
    @NotNull(message = "Enter the email of the user")
    private String email;
    @NotNull(message = "Enter a username")
    private String username;
    private String address;
    @NotNull(message = "Enter password")
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
}
