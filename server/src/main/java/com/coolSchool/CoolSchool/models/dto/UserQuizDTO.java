package com.coolSchool.CoolSchool.models.dto;

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
    private Long userId;
    private Long quizId;
    private BigDecimal grade;
    private String feedback;
    private LocalDateTime completedAt;
    private BigDecimal totalMarks;
    private Integer attemptNumber;
}
