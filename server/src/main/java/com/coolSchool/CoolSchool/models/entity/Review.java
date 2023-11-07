package com.coolSchool.CoolSchool.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Max(5)
    @Min(1)
    private Integer stars;
    @Size(max = 50)
    private String text;
    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;
}
