package com.coolSchool.coolSchool.models.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long subsectionId;
    private Integer attemptLimit;
    private BigDecimal totalMarks;
    private Integer quizDurationInMinutes;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long courseId;
}
