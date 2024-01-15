package com.coolSchool.coolSchool.models.dto.auth;

import com.coolSchool.coolSchool.enums.Provider;
import com.coolSchool.coolSchool.enums.Role;
import com.coolSchool.coolSchool.models.dto.request.CompleteOAuthRequest;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest extends CompleteOAuthRequest {
    private String email;
    private String password;
    private String username;
    private Provider provider = Provider.LOCAL;
}
