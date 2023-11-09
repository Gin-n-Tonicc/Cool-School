package com.coolSchool.CoolSchool.models.dto.response;

import com.coolSchool.CoolSchool.models.dto.common.CourseSubsectionDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseSubsectionResponseDTO extends CourseSubsectionDTO {
    @JsonProperty(value = "course")
    private CourseResponseDTO courseId;
}
