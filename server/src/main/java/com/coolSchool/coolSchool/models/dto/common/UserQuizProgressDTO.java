package com.coolSchool.coolSchool.models.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserQuizProgressDTO {
    private Long id;
    private Long questionId;
    private Long answerId;
    private Long quizId;
    private Long userId;
}
