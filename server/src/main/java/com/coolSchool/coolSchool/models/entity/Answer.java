package com.coolSchool.coolSchool.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@Table(name = "answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull(message = "The text of the answer should not be null!")
    @NotBlank(message = "The text of the answer should not be blank!")
    private String text;
    @ManyToOne
    @JoinColumn(name = "question_id")
    @NotNull(message = "The question not be null!")
    private Question questionId;
    @NotNull(message = "The isCorrect field of the answer should not be null!")
    private boolean isCorrect;
    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;
}
