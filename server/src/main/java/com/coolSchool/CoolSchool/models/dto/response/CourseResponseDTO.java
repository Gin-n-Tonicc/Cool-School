package com.coolSchool.CoolSchool.models.dto.response;

import com.coolSchool.CoolSchool.models.dto.CategoryDTO;
import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDTO {
    private Long id;
    private String name;
    private String aClass;
    private PublicUserDTO userId;
    private CategoryDTO categoryId;
    private double starts;
}
