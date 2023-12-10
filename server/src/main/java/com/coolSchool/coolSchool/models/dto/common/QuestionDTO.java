package com.coolSchool.coolSchool.models.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private Long id;
    private BigDecimal marks;
    private String description;
    @JsonProperty(value = "quiz")
    private Long quizId;
}
