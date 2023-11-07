package com.coolSchool.CoolSchool.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAndAnswersDTO {
    private QuestionDTO question;
    private List<AnswerDTO> answers;
}
