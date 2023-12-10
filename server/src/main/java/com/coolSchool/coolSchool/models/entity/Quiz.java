package com.coolSchool.coolSchool.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
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
    @NotNull(message = "The start time of the quiz should not be null!")
    private LocalDateTime startTime;
    @NotNull(message = "The end time of the quiz should not be null!")
    private LocalDateTime endTime;
    @NotNull(message = "The time limit of the quiz should not be null!")
    private Duration timeLimit;
    @ManyToOne
    @JoinColumn(name = "subsection_id")
    @NotNull(message = "The subsection of the quiz should not be null!")
    private CourseSubsection subsection;
    @NotNull(message = "The attempt limit of the quiz should not be null!")
    private Integer attemptLimit;
    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;
}
