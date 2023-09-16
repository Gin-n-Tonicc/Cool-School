package com.coolSchool.CoolSchool.models.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenBodyDTO {
    private String refreshToken;
}
