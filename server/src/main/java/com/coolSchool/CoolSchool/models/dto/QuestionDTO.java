package com.coolSchool.CoolSchool.models.dto;

import com.coolSchool.CoolSchool.models.entity.Quiz;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private BigDecimal marks;
    private String description;
    private Quiz quiz;
}
