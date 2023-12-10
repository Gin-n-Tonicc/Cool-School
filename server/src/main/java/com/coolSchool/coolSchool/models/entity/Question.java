package com.coolSchool.coolSchool.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull(message = "The marks of the question should not be null!")
    private BigDecimal marks;
    @NotBlank(message = "The description of the question should not be blank!")
    @NotNull(message = "The description of the question should not be null!")
    private String description;
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    @NotNull(message = "The quiz of the question should not be null!")
    private Quiz quiz;
    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;
}
