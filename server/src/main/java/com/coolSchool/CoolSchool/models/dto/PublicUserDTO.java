package com.coolSchool.CoolSchool.models.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicUserDTO {
    private Long id;
    private String firstname;
    private String email;
}
