package com.coolSchool.coolSchool.models.dto.response;

import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.common.CategoryDTO;
import com.coolSchool.coolSchool.models.dto.common.CourseDTO;
import com.coolSchool.coolSchool.models.entity.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDTO extends CourseDTO {
    private PublicUserDTO user;
    private CategoryDTO category;
    private File picture;
}
