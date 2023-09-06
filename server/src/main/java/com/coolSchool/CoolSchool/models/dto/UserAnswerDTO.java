package com.coolSchool.CoolSchool.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAnswerDTO {
    private Long id;
    private Long userId;
    private Long answerId;
    private Integer attemptNumber;
}
