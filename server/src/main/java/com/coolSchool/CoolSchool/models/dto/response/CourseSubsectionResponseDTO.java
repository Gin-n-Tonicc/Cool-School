package com.coolSchool.CoolSchool.models.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseSubsectionResponseDTO {
    private Long id;
    private String title;
    private String description;
    private CourseResponseDTO courseId;
}
