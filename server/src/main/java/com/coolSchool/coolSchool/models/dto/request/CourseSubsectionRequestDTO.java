package com.coolSchool.coolSchool.models.dto.request;

import com.coolSchool.coolSchool.models.dto.common.CourseSubsectionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseSubsectionRequestDTO extends CourseSubsectionDTO {
    private Long courseId;
    private Set<Long> resources;
}
