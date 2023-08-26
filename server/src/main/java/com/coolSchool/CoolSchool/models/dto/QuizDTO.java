package com.coolSchool.CoolSchool.models.dto;

import com.coolSchool.CoolSchool.models.entity.CourseSubsection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDTO {
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration timeLimit;
    private CourseSubsection subsection;
}
