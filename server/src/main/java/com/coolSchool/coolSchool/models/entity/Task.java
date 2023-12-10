package com.coolSchool.coolSchool.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull(message = "The title of the task should not be null!")
    private String title;
    private String description;
    private LocalDateTime deadline;
    @ManyToOne
    @JoinColumn(name = "subsection_id")
    private CourseSubsection subsection;
    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;
}
