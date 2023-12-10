package com.coolSchool.coolSchool.models.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserQuizDTO {
    private Long id;
    @JsonProperty(value = "user")
    private Long userId;
    @JsonProperty(value = "quiz")
    private Long quizId;
    private BigDecimal grade;
    private String feedback;
    private LocalDateTime completedAt;
    private BigDecimal totalMarks;
    private Integer attemptNumber;

}
