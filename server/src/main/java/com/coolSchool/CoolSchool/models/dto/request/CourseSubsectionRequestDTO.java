package com.coolSchool.CoolSchool.models.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseSubsectionRequestDTO {
    private Long id;
    private String title;
    private String description;
    private Long courseId;
}
