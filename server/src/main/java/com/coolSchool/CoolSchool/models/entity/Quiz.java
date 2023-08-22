package com.coolSchool.CoolSchool.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Timer;

@Entity
@Data
@NoArgsConstructor
@Table(name = "quizzes")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull(message = "The title of the quiz should not be null!")
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration timeLimit;
    @ManyToOne
    @JoinColumn(name = "subsection_id")
    private CourseSubsection subsection;
}
