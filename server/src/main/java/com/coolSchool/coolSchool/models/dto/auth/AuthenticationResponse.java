package com.coolSchool.coolSchool.models.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse implements Serializable {
    private String accessToken;
    private String refreshToken;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PublicUserDTO user;
}
