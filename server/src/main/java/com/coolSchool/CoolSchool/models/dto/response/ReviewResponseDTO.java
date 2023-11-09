package com.coolSchool.CoolSchool.models.dto.response;


import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.CoolSchool.models.dto.common.ReviewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDTO extends ReviewDTO {
    private PublicUserDTO user;
    private CourseResponseDTO course;
}
