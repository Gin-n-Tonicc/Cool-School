package com.coolSchool.coolSchool.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "course_subsections")
public class CourseSubsection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull(message = "The title of the courseSubsection should not be null!")
    private String title;
    @NotNull(message = "The description of the courseSubsection should not be null!")
    private String description;
    @OneToMany
    private Set<Resource> resources;
    @ManyToOne
    @JoinColumn(name = "course_id")
    @NotNull(message = "The course of the courseSubsection should not be null!")
    private Course course;
    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;
}
