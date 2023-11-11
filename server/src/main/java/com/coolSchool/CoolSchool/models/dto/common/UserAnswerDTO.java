package com.coolSchool.CoolSchool.models.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAnswerDTO {
    private Long id;
    @JsonProperty(value = "user")
    private Long userId;
    @JsonProperty(value = "answer")
    private Long answerId;
    private Integer attemptNumber;
}
