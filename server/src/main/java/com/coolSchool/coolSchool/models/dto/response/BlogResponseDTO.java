package com.coolSchool.coolSchool.models.dto.response;

import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.common.BlogDTO;
import com.coolSchool.coolSchool.models.dto.common.CategoryDTO;
import com.coolSchool.coolSchool.models.entity.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogResponseDTO extends BlogDTO {
    private List<PublicUserDTO> liked_users;
    private File picture;
    private PublicUserDTO owner;
    private CategoryDTO category;
}
