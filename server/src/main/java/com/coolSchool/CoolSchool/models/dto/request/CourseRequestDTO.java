package com.coolSchool.CoolSchool.models.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequestDTO {
    private Long id;
    private String name;
    private String aClass;
    private Long userId;
    private Long categoryId;
    private double starts;
}
