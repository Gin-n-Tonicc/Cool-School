package com.coolSchool.coolSchool.models.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizResultDTO {
    private Long quizId;
    private String quizTitle;
    private BigDecimal userScore;
    private List<QuestionAndAnswersDTO> questionAndAnswersList;
}