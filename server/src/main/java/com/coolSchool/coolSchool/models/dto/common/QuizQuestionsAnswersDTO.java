package com.coolSchool.coolSchool.models.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizQuestionsAnswersDTO {
    private QuizDTO quiz;
    private List<QuestionAndAnswersDTO> questions;
}
