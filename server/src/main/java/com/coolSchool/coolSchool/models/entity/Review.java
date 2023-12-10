package com.coolSchool.coolSchool.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "The user of the review should not be null!")
    private User user;
    @ManyToOne
    @JoinColumn(name = "course_id")
    @NotNull(message = "The course of the review should not be null!")
    private Course course;
    @Max(value = 5, message = "Stars must be between one and five!")
    @Min(value = 1, message = "Stars must be between one and five!")
    private Integer stars;
    @NotNull(message = "The review should not be null!")
    @NotBlank(message = "The review should not be blank!")
    @Size(max = 50)
    private String text;
    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;
}
