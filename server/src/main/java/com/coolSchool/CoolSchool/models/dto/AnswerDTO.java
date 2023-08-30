package com.coolSchool.CoolSchool.models.dto;

import com.coolSchool.CoolSchool.models.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {
    private String text;
    private Question question;
    private boolean isCorrect;
}
