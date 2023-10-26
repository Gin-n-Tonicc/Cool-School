package com.coolSchool.CoolSchool.models.dto.auth;

import com.coolSchool.CoolSchool.models.entity.File;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserDTO extends PublicUserDTO {
    private String lastname;
    private boolean deleted;
    private File profilePic;
    private String address;
}
