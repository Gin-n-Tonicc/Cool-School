package com.coolSchool.coolSchool.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "quizzes")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "The title of the quiz should not be blank!")
    @NotNull(message = "The title of the quiz should not be null!")
    private String title;

    @NotNull(message = "The description of the quiz should not be null!")
    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "subsection_id")
    @NotNull(message = "The subsection of the quiz should not be null!")
    private CourseSubsection subsection;

    @Min(value = 1, message = "Quiz attempt limit must be at least 1 try")
    private Integer attemptLimit;

    @Column(name = "quiz_duration_minutes")
    @Min(value = 5, message = "Quiz duration must be at least 5 minute")
    @Max(value = 300, message = "Quiz duration cannot exceed 300 minutes")
    private Integer quizDurationInMinutes;

    private BigDecimal totalMarks;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;
}
