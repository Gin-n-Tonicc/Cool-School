package com.coolSchool.coolSchool.models.dto.auth;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenBodyDTO {
    private String accessToken;
}
