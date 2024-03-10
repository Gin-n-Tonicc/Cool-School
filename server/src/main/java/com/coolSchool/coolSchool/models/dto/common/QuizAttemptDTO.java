package com.coolSchool.coolSchool.models.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizAttemptDTO {
    private Long id;
    private QuizDTO quiz;
    private List<UserAnswerDTO> userAnswers;
    private BigDecimal totalMarks;
    private int attemptNumber;
    private Long timeLeft;
    private Long remainingTimeInSeconds;
    private boolean completed;
}
