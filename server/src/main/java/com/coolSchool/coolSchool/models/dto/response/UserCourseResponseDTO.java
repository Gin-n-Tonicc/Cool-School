package com.coolSchool.coolSchool.models.dto.response;

import com.coolSchool.coolSchool.models.dto.common.UserCourseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCourseResponseDTO extends UserCourseDTO {
    private Long userId;
    @JsonProperty(value = "course")
    private CourseResponseDTO courseId;
    private BigDecimal quizSuccessPercentage;
}
