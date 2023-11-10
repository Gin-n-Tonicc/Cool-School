package com.coolSchool.CoolSchool.models.dto.response;

import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.CoolSchool.models.dto.common.CategoryDTO;
import com.coolSchool.CoolSchool.models.dto.common.CourseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDTO extends CourseDTO {
    @JsonProperty(value = "user")
    private PublicUserDTO userId;
    @JsonProperty(value = "category")
    private CategoryDTO categoryId;
}
