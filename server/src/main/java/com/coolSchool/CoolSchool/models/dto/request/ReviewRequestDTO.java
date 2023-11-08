package com.coolSchool.CoolSchool.models.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDTO {
    private Long id;
    private Long userId;
    private Long courseId;
    private Integer stars;
    private String text;
}
