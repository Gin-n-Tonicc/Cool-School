package com.coolSchool.CoolSchool.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class AuthenticationResponse implements Serializable {
  private String accessToken;
  private String refreshToken;
  private PublicUserDTO user;
}
