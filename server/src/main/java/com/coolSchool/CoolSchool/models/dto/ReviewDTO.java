package com.coolSchool.CoolSchool.models.dto;


import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.CoolSchool.models.entity.Course;
import com.coolSchool.CoolSchool.models.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long id;
    private PublicUserDTO user;
    private CourseDTO course;
    private Integer stars;
    private String text;
}
