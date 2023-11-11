package com.coolSchool.CoolSchool.models.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {
    private Long id;
    private String text;
    @JsonProperty(value = "question")
    private Long questionId;
    private boolean isCorrect;
}
