package com.coolSchool.coolSchool.models.dto.common;

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

    public AnswerDTO(Long id, String text, Long questionId) {
        this.id = id;
        this.text = text;
        this.questionId = questionId;
    }
}
