package com.coolSchool.CoolSchool.models.dto.request;

import com.coolSchool.CoolSchool.models.dto.common.CourseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequestDTO extends CourseDTO {
    private Long userId;
    private Long categoryId;
}
