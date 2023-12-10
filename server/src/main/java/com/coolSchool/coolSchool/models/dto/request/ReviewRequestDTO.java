package com.coolSchool.coolSchool.models.dto.request;

import com.coolSchool.coolSchool.models.dto.common.ReviewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDTO extends ReviewDTO {
    private Long userId;
    private Long courseId;
}
