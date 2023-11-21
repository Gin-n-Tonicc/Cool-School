package com.coolSchool.CoolSchool.models.dto.common;

import com.coolSchool.CoolSchool.models.entity.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseSubsectionDTO {
    private Long id;
    private String title;
    private String description;
}
