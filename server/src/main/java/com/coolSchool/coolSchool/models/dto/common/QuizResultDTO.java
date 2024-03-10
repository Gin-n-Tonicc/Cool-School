package com.coolSchool.coolSchool.models.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizResultDTO {
    private QuizAttemptDTO quizAttempt;
}