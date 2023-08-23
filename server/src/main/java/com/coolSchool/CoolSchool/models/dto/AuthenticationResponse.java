package com.coolSchool.CoolSchool.models.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class AuthenticationResponse implements Serializable {
    private String accessToken;
    private String refreshToken;
    private PublicUserDTO user;
}
