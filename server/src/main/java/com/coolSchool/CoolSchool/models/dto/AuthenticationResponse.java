package com.coolSchool.CoolSchool.models.dto;

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
    private PublicUserDTO user;
}
